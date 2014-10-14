import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import org.slf4j.MDC;

import java.util.UUID;


public class RequestTrackerClientFilter extends ClientFilter {

    @Override
    public ClientResponse handle(ClientRequest clientRequest) throws ClientHandlerException {
        // As getNext() is final in ClientFilter and cannot be stubbed in test classes, all work is
        // performed in the protected doWork method which can be unit tested.
        // That the filter hands off to the next in the chain can only be tested in an integration test
        return getNext().handle(doWork(clientRequest));
    }

    protected ClientRequest doWork(ClientRequest clientRequest) {
        String logId= MDC.get("Request-Tracker");

        if(logId == null) {
            logId = UUID.randomUUID().toString();
        }

        clientRequest.getHeaders().add("X-Request-Tracker", logId);

        return clientRequest;
    }
}
