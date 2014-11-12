package com.serviceenabled.dropwizardrequesttracker.com.serviceenabled.dropwizardrequesttracker.it;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;


public class RequestTrackerBundleIT {
	@ClassRule
	public static final DropwizardAppRule<Configuration> RULE = new DropwizardAppRule<Configuration>(TestApplication.class,null);
	
	@Test
	public void bundleAddsFilter(){
		Environment e = RULE.getEnvironment();
		assertThat(e.getApplicationContext().getServletHandler().getFilter("request-tracker-servlet-filter"),notNullValue());
	}

	@Test
	public void addsTrackerToOutogingRequest() throws Exception {
		Client client = new Client();
		URI initialURI = new URI("HTTP",null,"localhost",RULE.getLocalPort(),"/client-test",null,null);
		URI secondaryURI = new URI("HTTP",null,"localhost",RULE.getLocalPort(),"/mock-test",null,null);

		ClientTestResource clientTest = new ClientTestResource(secondaryURI);
		MockTestResource mockTest = new MockTestResource();

		RULE.getEnvironment().getApplicationContext().stop();
		RULE.getEnvironment().jersey().register(mockTest);
		RULE.getEnvironment().jersey().register(clientTest);
		RULE.getEnvironment().getApplicationContext().start();

		ClientResponse response = client.resource(initialURI).post(ClientResponse.class);
		assertThat(mockTest.getLogId(),notNullValue());
	}

}
