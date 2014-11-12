package com.serviceenabled.dropwizardrequesttracker.com.serviceenabled.dropwizardrequesttracker.it;

import com.serviceenabled.dropwizardrequesttracker.RequestTrackerClientFilter;
import com.serviceenabled.dropwizardrequesttracker.RequestTrackerConstants;
import com.sun.jersey.api.client.Client;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.net.URI;

/**
 * Created by rmuhic on 11/11/2014.
 */
@Path("/client-test")
public class ClientTestResource {
    private Client client;
    private URI uri;

    public ClientTestResource(URI uri, Client client){
        this.uri = uri;
        this.client = client;
    }

    @POST
    public void test(){
        client.resource(uri).post();
    }
}
