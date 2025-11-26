package com.demo.process.jsonplaceholder.cross;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Provider
public class IncomingRequestFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String method = containerRequestContext.getMethod();
        String path = containerRequestContext.getUriInfo().getAbsolutePath().toString();

        // 2. Puedes leer headers
        String correlationId = containerRequestContext.getHeaderString("X-Correlation-ID");

        log.info("[START] - request init: method:{}, path:{}", method, path);

    }
}
