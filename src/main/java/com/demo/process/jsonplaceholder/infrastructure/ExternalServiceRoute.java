package com.demo.process.jsonplaceholder.infrastructure;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ExternalServiceRoute extends RouteBuilder {

    @ConfigProperty(name = "external.service.url")
    String externalServiceUrl;

    @Override
    public void configure() throws Exception {


        from("direct:callExternalServiceGet")
                .log("headers: ${header.id} and token: ${header.token}")
                .setBody().simple("result: ${header.id}");

        from("direct:callExternalServicePost")
                .log("Recibiendo persona: ${body}")
                .setBody(constant("Success"));

    }
}
