package com.serviceenabled.dropwizardrequesttracker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.MDC;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.util.UUID;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class RequestTrackerClientFilterTest {
    private RequestTrackerClientFilter requestTrackerClientFilter;
    private RequestTrackerConfiguration configuration;

    @Mock private ClientRequestContext clientRequest;
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
    public void setsTheRequestTrackerHeader() {
        requestTrackerClientFilter.filter(clientRequest);

        verify(headersMap).add(eq(this.configuration.getHeaderName()), Mockito.any(UUID.class));
    }

    @Test
    public void usesExistingMDCValueWhenPresent() {
        String logId = UUID.randomUUID().toString();
        MDC.put(this.configuration.getMdcKey(), logId);
        requestTrackerClientFilter.filter(clientRequest);

        verify(headersMap).add(eq(this.configuration.getHeaderName()), eq(logId));
    }
}