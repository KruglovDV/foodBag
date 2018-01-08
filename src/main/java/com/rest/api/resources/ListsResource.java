package com.rest.api.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.mongodb.MongoException;
import com.rest.api.dal.DataAccess;
import com.rest.api.dal.IDataAccess;
import com.rest.api.model.POJOList;

import java.util.List;

@Path("lists")
public class ListsResource {

    private static IDataAccess dao = DataAccess.dao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLists() {
        final List<String> lists = dao.getLists();
        return Response.ok(lists).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCurrentList(final POJOList list) {
        if (list != null && list.items != null && dao.createCurrentList(list.items)) {
            list.items.forEach(dao::createItem);
            return Response.ok().build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("current")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentList() {
        return Response.ok(dao.getCurrentList()).build();
    }

    @PUT
    @Path("current")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(final POJOList list) {
        if (list.items != null || list.items.size() > 0) {
            try {
                list.items.forEach(dao::addItemToCurrent);
            } catch (final MongoException e) {
                System.out.println(e.getMessage());
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            return Response.ok().build();
        }
        if (dao.moveToPurchased(list.purchased))
            return Response.ok().build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @DELETE
    @Path("current")
    public Response setCurrentToInactive() {
        if (dao.setCurrentToInactive())
            return Response.ok().build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

}
