package com.demo.process.jsonplaceholder.cross;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Provider
public class OutgoingRequestFilter implements ClientRequestFilter {
    @Override
    public void filter(ClientRequestContext clientRequestContext) throws IOException {
        // Antes de que la solicitud salga
        String method = clientRequestContext.getMethod();
        String uri = clientRequestContext.getUri().toString();

        log.info("[PROCESS] - request #: method:{}, uri:{}", method, uri);

    }
}
