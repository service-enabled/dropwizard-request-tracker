package com.serviceenabled.dropwizardrequesttracker;

import java.util.Optional;
import java.util.function.Supplier;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import org.slf4j.MDC;


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
        Optional<String> requestId = Optional.ofNullable(MDC.get(configuration.getMdcKey()));

        clientRequest.getHeaders().add(configuration.getHeaderName(),
            requestId.orElseGet(idSupplier));
    }
}