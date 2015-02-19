package com.serviceenabled.dropwizardrequesttracker.com.serviceenabled.dropwizardrequesttracker.it;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.serviceenabled.dropwizardrequesttracker.RequestTrackerBundle;
import com.serviceenabled.dropwizardrequesttracker.RequestTrackerConfiguration;


public class BundleApplication extends Application<BundleConfiguration> {

	private final RequestTrackerBundle<BundleConfiguration> tracker = new RequestTrackerBundle<BundleConfiguration>() {
		@Override
		public RequestTrackerConfiguration getRequestTrackerConfiguration(BundleConfiguration configuration) {
			return configuration.getRequestTrackerConfiguration();
		}
	};
	
	@Override
	public void initialize(Bootstrap<BundleConfiguration> bootstrap) {
		bootstrap.addBundle(tracker);
	}

	@Override
	public void run(BundleConfiguration configuration, Environment environment) throws Exception {
		environment.jersey().register(new TestResource());
	}

}
