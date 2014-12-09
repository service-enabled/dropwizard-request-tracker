package com.serviceenabled.dropwizardrequesttracker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.MDC;

import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RequestTrackerServletFilterTest {
	private RequestTrackerServletFilter requestTrackerServletFilter;

	@Mock private HttpServletRequest request;
	@Mock private ServletResponse response;
	@Mock private FilterChain chain;

	@Before
	public void setUp() throws Exception {
		requestTrackerServletFilter = new RequestTrackerServletFilter();
	}

	@After
	public void tearDown() throws Exception {
		MDC.clear();
	}

	@Test
	public void callsChainDoFilter() throws Exception {
		requestTrackerServletFilter.doFilter(request, response, chain);

		verify(chain).doFilter(request, response);
	}

	@Test
	public void checksForHeader() throws Exception {
		requestTrackerServletFilter.doFilter(request, response, chain);

		verify(request).getHeader(RequestTrackerConstants.DEFAULT_LOG_ID_HEADER);
	}

	@Test
	public void checksForCustomHeader() throws Exception {
		RequestTrackerServletFilter requestTrackerServletFilter = new RequestTrackerServletFilter("foo");
		requestTrackerServletFilter.doFilter(request, response, chain);

		verify(request).getHeader("foo");
	}

	@Test
	public void setsIdWhenHeaderMissing() throws Exception {
		requestTrackerServletFilter.doFilter(request, response, chain);

		UUID.fromString(MDC.get(RequestTrackerConstants.MDC_KEY));
		// Didn't throw an IllegalArgumentException so it worked!
	}

	@Test
	public void reusesIdWhenHeaderPresent() throws Exception {
		String headerId = UUID.randomUUID().toString();
		when(request.getHeader(RequestTrackerConstants.DEFAULT_LOG_ID_HEADER)).thenReturn(headerId);

		requestTrackerServletFilter.doFilter(request, response, chain);

		String idInLog = MDC.get(RequestTrackerConstants.MDC_KEY);
		assertThat(idInLog, equalTo(headerId));
	}

}
