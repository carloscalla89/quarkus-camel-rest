package com.demo.process.jsonplaceholder.infrastructure;

import com.demo.process.jsonplaceholder.cross.HttpClientEnvelopeRouteBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ExternalServiceRoute extends HttpClientEnvelopeRouteBuilder {

    @Override
    protected void configureRoutes() throws Exception {

        from("direct:getPostById")
                .setHeader(HttpClientEnvelopeRouteBuilder.H_OUT_TYPE_FQN, constant(JsonPlaceHolderDtoResponse.class))
                .setHeader("_path", simple("/posts/${header.id}"))
                .toD("https://jsonplaceholder.typicode.com/posts/${header.id}");

        from("direct:createPost")
                .setBody(simple("${body}"))
                .setHeader(HttpClientEnvelopeRouteBuilder.H_MARSHAL_JSON, constant(true))
                .setHeader(HttpClientEnvelopeRouteBuilder.H_OUT_TYPE_FQN, constant(JsonPlaceHolderDtoResponse.class))
                .to("https://jsonplaceholder.typicode.com/posts");
    }
}
