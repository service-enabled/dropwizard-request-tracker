package com.serviceenabled.dropwizardrequesttracker;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;

public class RequestTrackerServletFilter implements Filter {
	// Use a supplier so we only generate id's when they're needed
	private final Supplier<String> idSupplier;
	private final RequestTrackerConfiguration configuration;

	public RequestTrackerServletFilter(RequestTrackerConfiguration configuration) {
		this(configuration, new UuidSupplier());
	}

	public RequestTrackerServletFilter(RequestTrackerConfiguration configuration, Supplier<String> idSupplier) {
		this.configuration = configuration;
		this.idSupplier = idSupplier;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest  = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		Optional<String> requestId = Optional.fromNullable(httpServletRequest.getHeader(configuration.getHeaderName()));
		String resolvedId = requestId.or(idSupplier);

		MDC.put(configuration.getMdcKey(), resolvedId);

		if( configuration.getAddResponseHeader() ) {
			httpServletResponse.addHeader( configuration.getHeaderName(), resolvedId );
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}
