package com.serviceenabled.dropwizardrequesttracker;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.MDC;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RequestTrackerClientFilterTest {
    @Rule public MockitoRule rule = MockitoJUnit.rule();

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
    public void tearDown() {
        MDC.clear();
    }

    @Test
    public void setsTheRequestTrackerHeader() {
        requestTrackerClientFilter.filter(clientRequest);

        verify(headersMap).add(eq(this.configuration.getHeaderName()), Mockito.any(String.class));
    }

    @Test
    public void usesExistingMDCValueWhenPresent() {
        String logId = UUID.randomUUID().toString();
        MDC.put(this.configuration.getMdcKey(), logId);
        requestTrackerClientFilter.filter(clientRequest);

        verify(headersMap).add(eq(this.configuration.getHeaderName()), eq(logId));
    }
}