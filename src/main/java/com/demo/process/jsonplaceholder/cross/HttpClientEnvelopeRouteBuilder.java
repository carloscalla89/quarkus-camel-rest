package com.demo.process.jsonplaceholder.cross;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonConstants;
import org.apache.camel.model.dataformat.JsonLibrary;

@ApplicationScoped
public abstract class HttpClientEnvelopeRouteBuilder extends RouteBuilder {

    public static final String H_OUT_TYPE_FQN = "X-OUT-TYPE";
    public static final String H_EXPECT_LIST = "X-EXPECT-LIST";
    public static final String H_MARSHAL_JSON = "X-MARSHAL-JSON";
    public static final String H_ORIG_URI = "_origUri";
    public static final String H_ENVELOPED = "X-ENVELOPED";

    @Inject
    @jakarta.inject.Named("logTrace")
    protected LoggerTrace loggerTrace;

    @Override
    public final void configure() throws Exception {
        configureInterceptors();
        configureRoutes();
    }

    protected void configureInterceptors() {
        //(Opcional) Manejo de errores con excepción (si usas throwExceptionOnFailure=true)
        // Genérico (SIEMPRE al final)
        onException(Exception.class)
                .onWhen(header(H_ENVELOPED).isEqualTo(true))
                .bean("logTrace", "onFailure")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .process(e -> {
                    var ex = e.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                    var out = new java.util.LinkedHashMap<String, Object>();
                    out.put("message", ex != null ? ex.getMessage() : "Internal error");
                    e.getMessage().setBody(out);// <--OBJETO, no String
                })
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .handled(true)
                .end();

        //TOP-LEVEL
        onCompletion()
                .onWhen(header(H_ENVELOPED).isEqualTo(true)) //filtra solo para tu envelope
                .bean("logTrace", "formatResponseRoot");

        interceptSendToEndpoint("http*")
                .onWhen(header(H_ENVELOPED).isNull())
                .process(ex -> {
                    //guarda la URI original
                    String orig = ex.getProperty(Exchange.TO_ENDPOINT, String.class);
                    String sep = orig.contains("?") ? "&" : "?";
                    String path = ex.getMessage().getHeader("_path", String.class);

                    if (path != null) {
                        ex.getMessage().setHeader("_origUri", orig + path + sep);
                    } else {
                        ex.getMessage().setHeader("_origUri", orig + sep);
                    }
                })
                .skipSendToOriginalEndpoint()
                .to("direct:httpEnvelope");

        //2)Sobre común: request & response processing
        from("direct:httpEnvelope")
                .routeId("global-http-envelope")
                //--- Request: limpieza y encabezados mínimos ---
                .removeHeaders("CamelHttp*")
                .removeHeader("Accept-Encoding")
                .removeHeader("Host")
                .removeHeader(Exchange.HTTP_URI)
                .removeHeader(Exchange.HTTP_PATH)
                .removeHeader(Exchange.HTTP_QUERY)
                .removeHeader(Exchange.HTTP_RAW_QUERY)
                .removeHeader(Exchange.HTTP_URL)
                .setHeader(H_ENVELOPED, constant(true))
                .process(ex -> {
                    //por defecto, si el llamador no puso método, intenta inferir
                    if (ex.getMessage().getHeader(Exchange.HTTP_METHOD) == null) {
                        // POST si caller puso MARSHAL_JSON=true (o ya hay body no vacío), si no GET
                        Object body = ex.getMessage().getBody();
                        Boolean needsMarshal = Boolean.TRUE.equals(ex.getMessage().getHeader(H_MARSHAL_JSON, Boolean.class));
                        String method = (needsMarshal || body != null) ? "POST" : "GET";
                    }

                    if (ex.getMessage().getHeader("Accept") == null) {
                        ex.getMessage().setHeader("Accept", "application/json");
                    }

                    if (ex.getMessage().getHeader(Exchange.CONTENT_TYPE) == null) {
                        ex.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json; charset=UTF-8");
                    }
                })
                //Serializa el request si body es POJO y llamador lo indicó
                .choice()
                    .when(header(H_MARSHAL_JSON).isEqualTo(true))
                    .marshal().json(JsonLibrary.Jackson)
                .end()
                // Llamada HTTP a la URI original ---
                .setProperty(Exchange.TO_ENDPOINT, simple("${header." + H_ORIG_URI + "}"))
                .process("logTrace")
                .toD("${header." + H_ORIG_URI + "}")

                //Si upstream devolvió error, envolver en DTO de error
                .choice()
                .when(simple("${header.CamelHttpResponseCode} >= 300"))
                .convertBodyTo(String.class)
                .process(e -> {
                    int code = e.getMessage().getHeader(Exchange.HTTP_RESPONSE_CODE, 500, Integer.class);
                    String raw = e.getMessage().getBody(String.class);
                    Object error = raw;
                    try {
                        error = getContext().getTypeConverter().mandatoryConvertTo(java.util.Map.class, raw);
                    } catch (Exception ignore) {
                    }
                    e.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
                    e.getMessage().setBody(new ErrorMessageDto(code, error));
                })
                .marshal().json(JsonLibrary.Jackson)
                .stop()
                .end()

                // --- Unmarshal dinámico según headers del llamador ---
                .choice()
                // Lista: produce List (sin genéricos estrictos)
                .when(header(H_EXPECT_LIST).isEqualTo(true))
                .setHeader(JacksonConstants.UNMARSHAL_TYPE, constant(java.util.List.class))
                .unmarshal().json(JsonLibrary.Jackson)
                .stop()
                // Objeto tipado: usa FQN en header X-OUT-TYPE
                .when(header(H_OUT_TYPE_FQN).isNotNull())
                .unmarshal().json(org.apache.camel.model.dataformat.JsonLibrary.Jackson)
                .end();
    }

    protected abstract void configureRoutes() throws Exception;
}