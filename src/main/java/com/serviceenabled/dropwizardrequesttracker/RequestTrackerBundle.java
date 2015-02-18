package com.serviceenabled.dropwizardrequesttracker;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

public abstract class RequestTrackerBundle<T> implements ConfiguredBundle<T> {

	@Override
	public void run(T configuration, Environment environment) throws Exception {
		RequestTrackerConfiguration requestTrackerConfiguration = this.getRequestTrackerConfiguration(configuration);
		environment.servlets()
				.addFilter("request-tracker-servlet-filter", new RequestTrackerServletFilter(requestTrackerConfiguration))
				.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "*");
	}

	@Override
	public void initialize(Bootstrap<?> bootstrap) {}
	
	public abstract RequestTrackerConfiguration getRequestTrackerConfiguration(T configuration);

}