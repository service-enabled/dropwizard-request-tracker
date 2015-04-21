package com.serviceenabled.dropwizardrequesttracker.it;

import com.serviceenabled.dropwizardrequesttracker.RequestTrackerBundle;
import com.serviceenabled.dropwizardrequesttracker.RequestTrackerConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class CustomIdSupplierBundleApplication extends Application<BundleConfiguration> {

	private final RequestTrackerBundle<BundleConfiguration> tracker = new RequestTrackerBundle<BundleConfiguration>(new CustomIdSupplier()) {
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
