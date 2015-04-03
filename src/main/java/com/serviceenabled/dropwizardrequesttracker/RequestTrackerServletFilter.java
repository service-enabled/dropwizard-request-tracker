package com.serviceenabled.dropwizardrequesttracker;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;

public class RequestTrackerServletFilter implements Filter {
	private final RequestTrackerConfiguration configuration;

	public RequestTrackerServletFilter(RequestTrackerConfiguration configuration) {
		this.configuration = configuration;
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		Optional<String> requestId = Optional.fromNullable(httpServletRequest.getHeader(configuration.getHeaderName()));
		MDC.put(configuration.getMdcKey(), requestId.or(getSupplier()));
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}

	private Supplier<String> getSupplier() {
		return this.configuration.getSupplierFactory().build();
	}
}