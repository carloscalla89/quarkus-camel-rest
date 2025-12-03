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

    @Inject
    JsonPlaceHolderBusinessService businessService;

    @GET
    @Path("/posts/{id}")
    public Response getPostById(@PathParam("id") String id) {

        var result = businessService.getPostById(id);
        return Response.ok(result).build();
    }

    @POST
    @Path("/post")
    public Response createPost(PostBodyDtoRequest request) {

        var result = businessService.createPost(request);
        return Response.status(Response.Status.CREATED).entity(result).build();
    }
}
