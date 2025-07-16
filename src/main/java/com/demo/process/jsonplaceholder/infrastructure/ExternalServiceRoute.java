package com.demo.process.jsonplaceholder.infrastructure;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.MDC;


@Slf4j
@ApplicationScoped
public class ExternalServiceRoute extends RouteBuilder {

    @ConfigProperty(name = "external.service.url")
    String externalServiceUrl;

    @Override
    public void configure() throws Exception {
// 1) REST interno expuesto con plataforma (platform-http o Undertow)


        from("direct:callExternalServiceGet")
                .routeId("call-jsonplaceholder")
                //.log("headers: ${header.id} and token: ${header.token}")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .removeHeader(Exchange.HTTP_PATH)
                .removeHeader(Exchange.HTTP_URI)
                .to("http://jsonplaceholder.typicode.com/posts/1?bridgeEndpoint=true")
                .unmarshal().json(org.apache.camel.model.dataformat.JsonLibrary.Jackson, Post.class)
                .log(">> Resultado: ${body}");

        from("direct:callExternalServicePost")
                .log("Recibiendo persona 2: ${body}")
                .setBody(constant("Success"));

    }

    public static class Post {
        public int userId;
        public int id;
        public String title;
        public String body;
        // getters y setters...
    }
}
