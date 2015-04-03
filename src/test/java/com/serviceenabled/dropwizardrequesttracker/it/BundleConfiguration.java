package com.serviceenabled.dropwizardrequesttracker.it;

import com.serviceenabled.dropwizardrequesttracker.RequestTrackerConfiguration;

import io.dropwizard.Configuration;

public class BundleConfiguration extends Configuration {

	private RequestTrackerConfiguration requestTrackerConfiguration = new RequestTrackerConfiguration();

	public RequestTrackerConfiguration getRequestTrackerConfiguration() {
		return requestTrackerConfiguration;
	}

	public void setRequestTrackerConfiguration(RequestTrackerConfiguration configuration) {
		this.requestTrackerConfiguration = configuration;
	}
}
