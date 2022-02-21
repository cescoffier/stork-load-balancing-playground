package org.acme;

import org.acme.services.Client;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/dispatcher")
public class Dispatcher {

    @RestClient
    Client client;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Fallback(fallbackMethod = "fallback")
    public String invoke() {
        return client.invoke();
    }

    static String fallback() {
        return "❌";
    }
}