package com.serviceenabled.dropwizardrequesttracker.it;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import com.serviceenabled.dropwizardrequesttracker.RequestTrackerConfiguration;

/**
 * Created by rmuhic on 11/11/2014.
 */
@Path("/mock-test")
public class MockTestResource {
    private HttpHeaders headers;

    public String getRequestTrackerId() {
        return headers.getRequestHeader(new RequestTrackerConfiguration().getHeaderName()).get(0);
    }
    
    public String getRequestTrackerId(String headerName) {
    	return headers.getRequestHeader(headerName).get(0);
    }

    @POST
    public void test(@Context HttpHeaders headers) {
        this.headers = headers;
    }
}
