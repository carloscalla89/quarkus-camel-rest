package com.demo.process.jsonplaceholder.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class MainRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("direct:mainRestGetRouter")
                .routeId("mainRestGetRouter")
                .log("callExternalServiceGet") // Log del ID recibido
                .to("direct:callExternalServiceGet");

        from("direct:mainRestPostRouter")
                .routeId("mainRestPostRouter")
                .log("callExternalServicePost") // Log del ID recibido
                .to("direct:callExternalServicePost");
    }
}
