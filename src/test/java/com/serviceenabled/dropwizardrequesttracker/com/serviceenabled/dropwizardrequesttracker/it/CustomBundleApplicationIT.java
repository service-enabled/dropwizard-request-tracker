package com.serviceenabled.dropwizardrequesttracker.com.serviceenabled.dropwizardrequesttracker.it;

import com.serviceenabled.dropwizardrequesttracker.RequestTrackerClientFilter;
import com.serviceenabled.dropwizardrequesttracker.RequestTrackerConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;


public class CustomBundleApplicationIT {
	@ClassRule
	public static final DropwizardAppRule<Configuration> RULE = new DropwizardAppRule<Configuration>(CustomBundleApplication.class, null);
	
	@Test
	public void bundleAddsFilter(){
		Environment e = RULE.getEnvironment();
		assertThat(e.getApplicationContext().getServletHandler().getFilter("request-tracker-servlet-filter"), notNullValue());
	}

	@Test
	public void addsTrackerToOutgoingRequest() throws Exception {
		Client client = new Client();
		client.addFilter(new RequestTrackerClientFilter("foo"));

		URI initialURI = new URI("HTTP", null, "localhost", RULE.getLocalPort(), "/client-test", null, null);
		URI secondaryURI = new URI("HTTP", null, "localhost", RULE.getLocalPort(), "/mock-test", null, null);

		ClientTestResource clientTest = new ClientTestResource(secondaryURI, client);
		MockTestResource mockTest = new MockTestResource();

		RULE.getEnvironment().getApplicationContext().stop();
		RULE.getEnvironment().jersey().register(mockTest);
		RULE.getEnvironment().jersey().register(clientTest);
		RULE.getEnvironment().getApplicationContext().start();

		client.resource(initialURI).post(ClientResponse.class);

		assertThat(mockTest.getHeaders().getRequestHeader("foo"), notNullValue());
		assertThat(mockTest.getHeaders().getRequestHeader(RequestTrackerConstants.DEFAULT_HEADER), nullValue());
	}

}
