package com.serviceenabled.dropwizardrequesttracker;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

@ExtendWith(MockitoExtension.class)
public class RequestTrackerServletFilterTest {
	private RequestTrackerServletFilter requestTrackerServletFilter;
	private RequestTrackerConfiguration configuration;

	@Mock private HttpServletRequest request;
	@Mock private HttpServletResponse response;
	@Mock private FilterChain chain;

	@BeforeEach
	public void setUp() {
		this.configuration = new RequestTrackerConfiguration();
		requestTrackerServletFilter = new RequestTrackerServletFilter(this.configuration);
	}

	@AfterEach
	public void tearDown() {
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

		verify(request).getHeader(configuration.getHeaderName());
	}

	@Test
	public void setsIdWhenHeaderMissing() throws Exception {
		requestTrackerServletFilter.doFilter(request, response, chain);

		UUID.fromString(MDC.get(configuration.getMdcKey()));
		// Didn't throw an IllegalArgumentException so it worked!
	}

	@Test
	public void reusesIdWhenHeaderPresent() throws Exception {
		String headerId = UUID.randomUUID().toString();
		when(request.getHeader(configuration.getHeaderName())).thenReturn(headerId);

		requestTrackerServletFilter.doFilter(request, response, chain);

		String idInLog = MDC.get(configuration.getMdcKey());
		assertThat(idInLog, equalTo(headerId));
	}

	@Test
	public void addsResponseHeaderWhenConfigured() throws Exception {
		this.configuration.setAddResponseHeader(true);

		//Re-injecting the configuration to ensure the updated configuration is applied.
		requestTrackerServletFilter = new RequestTrackerServletFilter(this.configuration);
		requestTrackerServletFilter.doFilter(request, response, chain);

		verify(response).addHeader( configuration.getHeaderName(), MDC.get(configuration.getMdcKey()));
	}
}
