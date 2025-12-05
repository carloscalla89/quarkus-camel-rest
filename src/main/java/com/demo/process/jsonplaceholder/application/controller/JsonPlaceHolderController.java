package com.demo.process.jsonplaceholder.application.controller;

import com.demo.process.jsonplaceholder.application.dto.PostBodyDtoRequest;
import com.demo.process.jsonplaceholder.application.service.JsonPlaceHolderBusinessService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/rest/jsonplaceholder")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JsonPlaceHolderController {

    // Inyectamos la lógica de negocio (reemplaza a los rutas direct:...)
    @Inject
    JsonPlaceHolderBusinessService businessService;

    // 2. Reemplaza .get("/posts/{id}")
    @GET
    @Path("/posts/{id}")
    public Response getPostById(@PathParam("id") String id) {
        // Aquí llamas a la lógica que antes estaba en "direct:getPostById"
        var result = businessService.getPostById(id);
        return Response.ok(result).build();
    }

    // 3. Reemplaza .post("/post") y .type(PostBodyDtoRequest.class)
    @POST
    @Path("/post")
    public Response createPost(PostBodyDtoRequest request) {
        // Aquí llamas a la lógica que antes estaba en "direct:createPost"
        var result = businessService.createPost(request);
        return Response.status(Response.Status.CREATED).entity(result).build();
    }
}
