package com.serviceenabled.dropwizardrequesttracker.com.serviceenabled.dropwizardrequesttracker.it;

import java.net.URI;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.sun.jersey.api.client.Client;

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
