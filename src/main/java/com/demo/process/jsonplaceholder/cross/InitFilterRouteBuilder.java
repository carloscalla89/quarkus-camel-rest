package com.demo.process.jsonplaceholder.cross;

import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public abstract class InitFilterRouteBuilder extends RouteBuilder {

    @Inject private RequestContextProcessor requestContextProcessor;
    @Inject private LoggerTrace loggerTrace;

    @Override
    public final void configure() throws Exception {

        restConfiguration()
                .component("platform-http")
                .bindingMode(RestBindingMode.json)
                .skipBindingOnErrorCode(false)
                .apiContextPath("/q/openapi")
                .apiProperty("api.title", "RESTful Service :: ")
                .apiProperty("api.version", "1.0")
                .apiProperty("openapi.version", "3.0.0")
                .apiProperty("cors", "true");

        configureInterceptors();
        configureRestRoutes();

    }

    protected void configureInterceptors() {
        interceptFrom()
                .process(requestContextProcessor)
                .process(loggerTrace::formatRequestRoot)
                .end();
    }

    protected abstract void configureRestRoutes() throws Exception;
}
