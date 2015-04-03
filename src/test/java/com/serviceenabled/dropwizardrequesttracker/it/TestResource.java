package com.serviceenabled.dropwizardrequesttracker.it;

import javax.ws.rs.POST;
import javax.ws.rs.Path;


@Path("/test")
public class TestResource {

    @POST
    public String test(){
        return "test";
    }
}
