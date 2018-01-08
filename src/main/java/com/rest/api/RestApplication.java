package com.rest.api;

import com.rest.api.resources.ItemsResource;
import com.rest.api.resources.ListsResource;
import com.rest.api.resources.TestResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("api")
public class RestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(ItemsResource.class);
        classes.add(ListsResource.class);
        classes.add(TestResource.class);
        return classes;
    }
}
