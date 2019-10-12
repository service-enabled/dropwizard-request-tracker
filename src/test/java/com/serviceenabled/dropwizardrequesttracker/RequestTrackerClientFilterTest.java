package com.serviceenabled.dropwizardrequesttracker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.util.UUID;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class RequestTrackerClientFilterTest {
    private RequestTrackerClientFilter requestTrackerClientFilter;
    private RequestTrackerConfiguration configuration;

    @Mock private ClientRequestContext clientRequest;
    @Mock private MultivaluedMap<String,Object> headersMap;

    @BeforeEach
    public void setUp() {
    	this.configuration = new RequestTrackerConfiguration();
        requestTrackerClientFilter = new RequestTrackerClientFilter(this.configuration);
        when(clientRequest.getHeaders()).thenReturn(headersMap);
    }

    @AfterEach
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