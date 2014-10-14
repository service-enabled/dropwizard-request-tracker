import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RequestTrackerServletFilterTest {
	private RequestTrackerServletFilter requestTrackerServletFilter;

	private HttpServletRequest request = mock(HttpServletRequest.class);
	private ServletResponse response = mock(ServletResponse.class);
	private FilterChain chain = mock(FilterChain.class);

	@Before
	public void setUp() throws Exception {
		requestTrackerServletFilter = new RequestTrackerServletFilter();
	}

	@After
	public void tearDown() throws Exception {
		MDC.clear();
		reset(request, response, chain);
	}

	@Test
	public void callsChainDoFilter() throws Exception {
		requestTrackerServletFilter.doFilter(request, response, chain);

		verify(chain).doFilter(request, response);
	}

	@Test
	public void checksForHeader() throws Exception {
		requestTrackerServletFilter.doFilter(request, response, chain);

		verify(request).getHeader("X-Request-Tracker");
	}

	@Test
	public void setsIdWhenHeaderMissing() throws Exception {
		requestTrackerServletFilter.doFilter(request, response, chain);

		UUID.fromString(MDC.get("Request-Tracker"));
		// Didn't throw an IllegalArgumentException so it worked!
	}

	@Test
	public void reusesIdWhenHeaderPresent() throws Exception {
		String headerId = UUID.randomUUID().toString();
		when(request.getHeader("X-Request-Tracker")).thenReturn(headerId);

		requestTrackerServletFilter.doFilter(request, response, chain);

		String idInLog = MDC.get("Request-Tracker");
		assertThat(idInLog, equalTo(headerId));
	}
}
