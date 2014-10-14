import com.sun.jersey.api.client.ClientHandler;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.slf4j.MDC;

import javax.ws.rs.core.MultivaluedMap;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


public class RequestTrackerClientFilterTest {
    private RequestTrackerClientFilter requestTrackerClientFilter;

    private ClientRequest clientRequest = mock(ClientRequest.class);
    private MultivaluedMap<String,Object> headersMap = mock(MultivaluedMap.class);

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
        verify(headersMap).add(eq("X-Request-Tracker"), Mockito.any(UUID.class));
    }

    @Test
    public void usesExistingMDCValueWhenPresent() {
        String logId = UUID.randomUUID().toString();
        MDC.put("Request-Tracker",logId);
        requestTrackerClientFilter.doWork(clientRequest);
        verify(headersMap).add(eq("X-Request-Tracker"), eq(logId));
    }
}
