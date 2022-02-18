package org.acme.services;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@RegisterRestClient(baseUri = "stork://my-service")
public interface Client {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String invoke();
}