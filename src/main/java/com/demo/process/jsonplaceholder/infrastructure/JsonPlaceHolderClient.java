package com.demo.process.jsonplaceholder.infrastructure;


import com.demo.process.jsonplaceholder.application.dto.PostBodyDtoRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface JsonPlaceHolderClient {

    // 2. Reemplaza "from("direct:getPostById")"
    // Endpoint: /posts/{id} y método GET
    // El valor de 'id' se pasa al PathParam
    @GET
    @Path("/posts/{id}")
    JsonPlaceHolderDtoResponse getPostById(@PathParam("id") String id);

    // 3. Reemplaza "from("direct:createPost")"
    // Endpoint: /posts (Post al root de la colección) y método POST
    // El body se pasa como argumento del método
    @POST
    @Path("/posts")
    JsonPlaceHolderDtoResponse createPost(PostBodyDtoRequest request);
}
