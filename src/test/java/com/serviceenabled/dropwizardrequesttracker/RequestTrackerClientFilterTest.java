package com.serviceenabled.dropwizardrequesttracker;

import com.sun.jersey.api.client.ClientRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.MDC;

import javax.ws.rs.core.MultivaluedMap;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class RequestTrackerClientFilterTest {
    private RequestTrackerClientFilter requestTrackerClientFilter;

    @Mock private ClientRequest clientRequest;
    @Mock private MultivaluedMap<String,Object> headersMap;

    @Before
    public void setUp() {
        requestTrackerClientFilter = new RequestTrackerClientFilter();
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

        verify(headersMap).add(eq(RequestTrackerConstants.DEFAULT_HEADER), Mockito.any(UUID.class));
    }

    @Test
    public void usesExistingMDCValueWhenPresent() {
        String logId = UUID.randomUUID().toString();
        MDC.put(RequestTrackerConstants.DEFAULT_MDC_KEY,logId);
        requestTrackerClientFilter.doWork(clientRequest);

        verify(headersMap).add(eq(RequestTrackerConstants.DEFAULT_HEADER), eq(logId));
    }

    @Test
    public void allowsHeaderToBeCustomized() {
        RequestTrackerClientFilter requestTrackerClientFilter = new RequestTrackerClientFilter("foo");
        when(clientRequest.getHeaders()).thenReturn(headersMap);
        requestTrackerClientFilter.doWork(clientRequest);

        verify(headersMap).add(eq("foo"), Mockito.any(UUID.class));
    }
}
