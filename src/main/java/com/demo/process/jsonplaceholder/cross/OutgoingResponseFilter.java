package com.demo.process.jsonplaceholder.cross;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Provider
public class OutgoingResponseFilter implements ClientResponseFilter {
    @Override
    public void filter(ClientRequestContext clientRequestContext, ClientResponseContext clientResponseContext) throws IOException {
        int status = clientResponseContext.getStatus();

        log.info("[PROCESS] - response #: status:{}", status);

        // Puedes inspeccionar el cuerpo de la respuesta o manejar errores HTTP aquí
        if (status >= 400) {
            // Lógica para registrar errores o lanzar una excepción de negocio
        }
    }
}
