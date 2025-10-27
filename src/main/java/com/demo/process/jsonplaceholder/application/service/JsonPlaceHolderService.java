package com.demo.process.jsonplaceholder.application.service;

import com.demo.process.jsonplaceholder.application.dto.PostBodyDtoRequest;
import com.demo.process.jsonplaceholder.cross.InitFilterRouteBuilder;
import jakarta.enterprise.context.Dependent;

@Dependent
public class JsonPlaceHolderService extends InitFilterRouteBuilder {

    @Override
    protected void configureRestRoutes() throws Exception {

        rest("/camel-rest/jsonplaceholder")
                .tag("demo camel rest")
                .description("API de prueba")

                .get("/posts/{id}")
                .routeId("get Post ID")
                .to("direct:getPostById")

                .post("/post")
                .routeId("post")
                .type(PostBodyDtoRequest.class)
                .to("direct:createPost");
        ;
    }
}
