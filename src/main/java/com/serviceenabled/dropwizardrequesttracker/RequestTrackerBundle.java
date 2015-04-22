package com.serviceenabled.dropwizardrequesttracker;

import com.google.common.base.Supplier;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

public abstract class RequestTrackerBundle<T> implements ConfiguredBundle<T> {
	private final Supplier<String> idSupplier;

	public RequestTrackerBundle() {
		this.idSupplier = new UuidSupplier();
	}

	public RequestTrackerBundle(Supplier<String> idSupplier) {
		this.idSupplier = idSupplier;
	}

	@Override
	public void run(T configuration, Environment environment) throws Exception {
		RequestTrackerConfiguration requestTrackerConfiguration = this.getRequestTrackerConfiguration(configuration);
		environment.servlets()
				.addFilter("request-tracker-servlet-filter", new RequestTrackerServletFilter(requestTrackerConfiguration, idSupplier))
				.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "*");
	}

	@Override
	public void initialize(Bootstrap<?> bootstrap) {}
	
	public abstract RequestTrackerConfiguration getRequestTrackerConfiguration(T configuration);

}