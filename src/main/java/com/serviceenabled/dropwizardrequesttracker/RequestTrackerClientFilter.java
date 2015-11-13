package com.serviceenabled.dropwizardrequesttracker;

import org.slf4j.MDC;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;


public class RequestTrackerClientFilter implements ClientRequestFilter {
    private final RequestTrackerConfiguration configuration;
    private final Supplier<String> idSupplier;

    public RequestTrackerClientFilter(RequestTrackerConfiguration configuration) {
        this(configuration, new UuidSupplier());
    }

    public RequestTrackerClientFilter(RequestTrackerConfiguration configuration, Supplier<String> idSupplier) {
        this.configuration = configuration;
        this.idSupplier = idSupplier;
    }

    @Override
    public void filter(ClientRequestContext clientRequest) {
        Optional<String> requestId = Optional.fromNullable(MDC.get(configuration.getMdcKey()));

        clientRequest.getHeaders().add(configuration.getHeaderName(), requestId.or(idSupplier));
    }
}