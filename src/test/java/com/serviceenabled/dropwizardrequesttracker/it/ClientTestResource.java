package com.serviceenabled.dropwizardrequesttracker.it;

import java.net.URI;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;


/**
 * Created by rmuhic on 11/11/2014.
 */
@Path("/client-test")
public class ClientTestResource {
    private Client client;
    private URI uri;

    public ClientTestResource(){
    }

    public ClientTestResource(URI uri, Client client){
        this.uri = uri;
        this.client = client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @POST
    public void test(){
        client.target(uri).request().post(Entity.json(null));
    }
}
