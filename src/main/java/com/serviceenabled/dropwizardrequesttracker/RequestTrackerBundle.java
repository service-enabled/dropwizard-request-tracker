package com.serviceenabled.dropwizardrequesttracker;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.serviceenabled.dropwizardrequesttracker.RequestTrackerServletFilter;


public class RequestTrackerBundle<T> implements ConfiguredBundle<T> {

	@Override
	public void run(T configuration, Environment environment) throws Exception {
		environment.servlets()
			.addFilter("request-tracker-servlet-filter", RequestTrackerServletFilter.class);
	}

	@Override
	public void initialize(Bootstrap<?> bootstrap) {
		
	}

}
