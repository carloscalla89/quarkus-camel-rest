package com.demo.process.jsonplaceholder.application.service;

import com.demo.process.jsonplaceholder.application.dto.PostBodyDtoRequest;
import com.demo.process.jsonplaceholder.cross.OutgoingRequestFilter;
import com.demo.process.jsonplaceholder.cross.OutgoingResponseFilter;
import com.demo.process.jsonplaceholder.infrastructure.JsonPlaceHolderClient;
import com.demo.process.jsonplaceholder.infrastructure.JsonPlaceHolderDtoResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.net.URI;

@ApplicationScoped
public class JsonPlaceHolderBusinessService {

    private JsonPlaceHolderClient getClient(String baseUrl) {
        // Creamos una nueva instancia del cliente en tiempo de ejecución
        return RestClientBuilder.newBuilder()
                .baseUri(URI.create(baseUrl)) // Aquí establecemos la URL dinámica
                .register(OutgoingRequestFilter.class)
                .register(OutgoingResponseFilter.class)
                .build(JsonPlaceHolderClient.class);
    }

    // Lógica que reemplaza "direct:getPostById"
    public JsonPlaceHolderDtoResponse getPostById(String id) {
        // La URL se determina aquí (podría venir de una BD, de una variable de entorno, etc.)
        String url = "https://jsonplaceholder.typicode.com";

        JsonPlaceHolderClient client = getClient(url);

        // Llamada simple y tipada
        return client.getPostById(id);
    }

    // Lógica que reemplaza "direct:createPost"
    public JsonPlaceHolderDtoResponse createPost(PostBodyDtoRequest request) {
        // La URL se determina aquí
        String url = "https://jsonplaceholder.typicode.com";

        JsonPlaceHolderClient client = getClient(url);

        // Llamada simple y tipada
        return client.createPost(request);
    }
}
