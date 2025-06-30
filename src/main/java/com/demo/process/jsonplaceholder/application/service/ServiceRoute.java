package com.demo.process.jsonplaceholder.application.service;

import com.demo.process.jsonplaceholder.application.dto.OpportunityQueryRequest;
import com.demo.process.jsonplaceholder.application.filters.BaseRouteBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

@ApplicationScoped
public class ServiceRoute extends BaseRouteBuilder {

    @Override
    public void configureBusinessRoutes() throws Exception {

        /*
        interceptFrom("rest:*")
                .log(LoggingLevel.INFO,"filters to print");

         */

        rest("/camel-rest/jsonplaceholder/posts")
                .get("/{id}")
                .routeId("restGetRouter")
                .to("direct:mainRestGetRouter");

        rest("/camel-rest/jsonplaceholder/posts")
                .post()
                .routeId("restPostRouter")
                .type(OpportunityQueryRequest.class) // Convierte automáticamente JSON a objeto Person
                .to("direct:mainRestPostRouter");



    }
}
