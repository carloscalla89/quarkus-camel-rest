package com.demo.process.jsonplaceholder.application.filters;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;

public abstract class BaseRouteBuilder extends RouteBuilder {
    private static final Logger LOG = Logger.getLogger(BaseRouteBuilder.class);

    @Override
    public final void configure() throws Exception {
        configureInterceptors();
        configureBusinessRoutes();
    }

    /** Define aquí TODO el interceptFrom genérico */
    protected void configureInterceptors() {

        getContext().setStreamCaching(true);

        interceptFrom()
                .process(exchange -> {
                    String traceId    = exchange.getIn().getHeader("trace-id", String.class);
                    String method = exchange.getIn().getHeader(Exchange.HTTP_METHOD, String.class);
                    String rootPath = exchange.getContext()
                            .resolvePropertyPlaceholders("{{quarkus.http.root-path}}");
                    String path = exchange.getIn().getHeader(Exchange.HTTP_PATH, String.class);
                    String body = exchange.getMessage().getBody(String.class);

                    if (traceId != null ) MDC.put("traceCustomId", traceId);

                    MDC.put("method", method);
                    MDC.put("path", path);
                    MDC.put("baseUri", rootPath);

                    if (body != null) {
                        MDC.put("body", body);
                    }

                });


    }

    /** Las rutas específicas de cada implementación */
    protected abstract void configureBusinessRoutes() throws Exception;
}
