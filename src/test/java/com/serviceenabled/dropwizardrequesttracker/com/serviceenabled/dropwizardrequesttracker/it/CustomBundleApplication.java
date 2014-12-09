package com.serviceenabled.dropwizardrequesttracker.com.serviceenabled.dropwizardrequesttracker.it;

import com.serviceenabled.dropwizardrequesttracker.RequestTrackerBundle;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class CustomBundleApplication extends Application<Configuration> {

	@Override
	public void initialize(Bootstrap<Configuration> bootstrap) {
		bootstrap.addBundle(new RequestTrackerBundle<Configuration>("foo"));
	}

	@Override
	public void run(Configuration configuration, Environment environment) throws Exception {
		environment.jersey().register(new TestResource());
	}

}
