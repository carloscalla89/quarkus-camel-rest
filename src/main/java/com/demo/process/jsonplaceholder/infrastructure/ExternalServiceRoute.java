package com.demo.process.jsonplaceholder.infrastructure;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
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


        from("direct:callExternalServiceGet")
                .log("headers: ${header.id} and token: ${header.token}")
                .setBody().simple("result: ${header.id}");

        from("direct:callExternalServicePost")
                .log(LoggingLevel.ERROR, "ERROR")
                .log("Recibiendo persona 2: ${body}")
                .process(resp -> {
                    MDC.put("requestid","TESTING");
                })
                .setBody(constant("Success"));

    }
}
