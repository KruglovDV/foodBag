package com.rest.api.resources;

import com.rest.api.dal.DataAccess;
import com.rest.api.dal.IDataAccess;
import com.rest.api.model.Item;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("items")
public class ItemsResource {

    private static IDataAccess dao = DataAccess.dao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItems() {
        final List<String> items = dao.getItems();
        return Response.ok(items).build();
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItem(@PathParam("name") final String name) {
        final String item = dao.getItem(name);
        return Response.ok(item).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createItem(final Item item) {
        if (dao.createItem(item.name))
            return Response.status(Response.Status.CREATED).build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPriceToItem(final Item item) {
        if (dao.addPriceToItem(item.name, item.date,item.price))
            return Response.ok().build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
