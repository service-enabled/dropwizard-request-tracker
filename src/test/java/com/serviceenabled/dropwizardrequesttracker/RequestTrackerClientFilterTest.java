package com.serviceenabled.dropwizardrequesttracker;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.MDC;

import com.sun.jersey.api.client.ClientRequest;


@RunWith(MockitoJUnitRunner.class)
public class RequestTrackerClientFilterTest {
    private RequestTrackerClientFilter requestTrackerClientFilter;
    private RequestTrackerConfiguration configuration;

    @Mock private ClientRequest clientRequest;
    @Mock private MultivaluedMap<String,Object> headersMap;

    @Before
    public void setUp() {
    	this.configuration = new RequestTrackerConfiguration();
        requestTrackerClientFilter = new RequestTrackerClientFilter(this.configuration);
        when(clientRequest.getHeaders()).thenReturn(headersMap);
    }

    @After
    public void tearDown() throws Exception {
        MDC.clear();
    }

    @Test
    public void returnsClientRequest() {
        assertThat(requestTrackerClientFilter.doWork(clientRequest), any(ClientRequest.class));
    }

    @Test
    public void setsTheRequestTrackerHeader() {
        requestTrackerClientFilter.doWork(clientRequest);

        verify(headersMap).add(eq(this.configuration.getHeaderName()), Mockito.any(UUID.class));
    }

    @Test
    public void usesExistingMDCValueWhenPresent() {
        String logId = UUID.randomUUID().toString();
        MDC.put(this.configuration.getMdcKey(), logId);
        requestTrackerClientFilter.doWork(clientRequest);

        verify(headersMap).add(eq(this.configuration.getHeaderName()), eq(logId));
    }
}