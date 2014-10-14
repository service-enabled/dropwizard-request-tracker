import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import org.slf4j.MDC;

import java.util.UUID;


public class RequestTrackerClientFilter extends ClientFilter {
    private static final Supplier<String> ID_SUPPLIER = new Supplier<String>() {
        @Override
        public String get() {
            return UUID.randomUUID().toString();
        }
    };

    @Override
    public ClientResponse handle(ClientRequest clientRequest) throws ClientHandlerException {
        // As getNext() is final in ClientFilter and cannot be stubbed in test classes, all work is
        // performed in the protected doWork method which can be unit tested.
        // That the filter hands off to the next in the chain can only be tested in an integration test
        return getNext().handle(doWork(clientRequest));
    }

    protected ClientRequest doWork(ClientRequest clientRequest) {
        Optional<String> requestId = Optional.fromNullable(MDC.get("Request-Tracker"));

        clientRequest.getHeaders().add("X-Request-Tracker", requestId.or(ID_SUPPLIER));

        return clientRequest;
    }
}
