import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RequestTrackerServletFilterTest {
	private RequestTrackerServletFilter requestTrackerServletFilter;

	private HttpServletRequest request = mock(HttpServletRequest.class);
	private ServletResponse response = mock(ServletResponse.class);
	private FilterChain chain = mock(FilterChain.class);

	@Before
	public void setUp() throws Exception {
		requestTrackerServletFilter = new RequestTrackerServletFilter();
	}

	@Test
	public void callsChainDoFilter() throws Exception {
		requestTrackerServletFilter.doFilter(request, response, chain);

		verify(chain).doFilter(request, response);
	}

	@Test
	public void setsIdWhenHeaderMissing() throws Exception {
		requestTrackerServletFilter.doFilter(request, response, chain);

		verify(request).getHeader("X-Request-Tracker");
		UUID requestId = UUID.fromString(MDC.get("Request-Tracker"));
		assertThat(requestId, any(UUID.class));
	}
}
