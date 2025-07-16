package com.demo.process.jsonplaceholder.application.service;

import com.demo.process.jsonplaceholder.application.dto.OpportunityQueryRequest;
import com.demo.process.jsonplaceholder.application.filters.BaseRouteBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.model.rest.RestBindingMode;


@ApplicationScoped
public class ServiceRoute extends BaseRouteBuilder {

    @Override
    public void configureBusinessRoutes() throws Exception {

        restConfiguration()
                .component("platform-http")
                .bindingMode(RestBindingMode.auto);

        rest("/camel-rest/jsonplaceholder/posts")
                .get("/{id}")
                .routeId("restGetRouter")
                .to("direct:callExternalServiceGet");

        rest("/camel-rest/jsonplaceholder/posts")
                .post()
                .routeId("restPostRouter")
                .type(OpportunityQueryRequest.class) // Convierte automáticamente JSON a objeto Person
                .to("direct:mainRestPostRouter");



    }
}
