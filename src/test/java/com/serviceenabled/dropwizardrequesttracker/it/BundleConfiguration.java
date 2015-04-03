package com.serviceenabled.dropwizardrequesttracker.it;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.serviceenabled.dropwizardrequesttracker.RequestTrackerConfiguration;

import io.dropwizard.Configuration;

public class BundleConfiguration extends Configuration {

	@JsonProperty("requestTracker")
	private RequestTrackerConfiguration requestTrackerConfiguration = new RequestTrackerConfiguration();

	public RequestTrackerConfiguration getRequestTrackerConfiguration() {
		return requestTrackerConfiguration;
	}

	public void setRequestTrackerConfiguration(RequestTrackerConfiguration configuration) {
		this.requestTrackerConfiguration = configuration;
	}
}
