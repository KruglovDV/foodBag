package com.rest.api.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;


@Path("test")
public class TestResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response test() {
        final List<String> testList = new ArrayList<>();
        testList.add("dima");
        testList.add("julia");
        return Response.ok(testList).build();
    }
}