package com.serviceenabled.dropwizardrequesttracker.com.serviceenabled.dropwizardrequesttracker.it;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.serviceenabled.dropwizardrequesttracker.RequestTrackerClientFilter;
import com.serviceenabled.dropwizardrequesttracker.RequestTrackerConstants;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.junit.DropwizardAppRule;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.net.URI;
import java.util.UUID;


public class DefaultBundleApplicationIT {
	@ClassRule
	public static final DropwizardAppRule<Configuration> RULE = new DropwizardAppRule<Configuration>(DefaultBundleApplication.class,null);
	private static final Client CLIENT = new Client();
	private static ClientTestResource CLIENT_TEST;
	private static MockTestResource MOCK_TEST;
	private static URI INITIAL_URI;

	@BeforeClass
	public static void setupApplication() throws Exception {
		CLIENT.addFilter(new RequestTrackerClientFilter());

		INITIAL_URI = new URI("HTTP",null,"localhost",RULE.getLocalPort(),"/client-test",null,null);
		URI secondaryURI = new URI("HTTP",null,"localhost",RULE.getLocalPort(),"/mock-test",null,null);

		CLIENT_TEST = new ClientTestResource(secondaryURI, CLIENT);
		MOCK_TEST = new MockTestResource();

		RULE.getEnvironment().getApplicationContext().stop();
		RULE.getEnvironment().jersey().register(MOCK_TEST);
		RULE.getEnvironment().jersey().register(CLIENT_TEST);
		RULE.getEnvironment().getApplicationContext().start();
	}
	
	@Test
	public void bundleAddsFilter(){
		Environment e = RULE.getEnvironment();
		assertThat(e.getApplicationContext().getServletHandler().getFilter("request-tracker-servlet-filter"), notNullValue());
	}

	@Test
	public void addsTrackerToOutgoingRequest() throws Exception {
		CLIENT.resource(INITIAL_URI).post(ClientResponse.class);

		assertThat(MOCK_TEST.getRequestTrackerId(), notNullValue());
	}

	@Test
	public void keepsTheId() throws Exception {
		Client client = new Client();
		UUID id = UUID.randomUUID();
		client.resource(INITIAL_URI).header(RequestTrackerConstants.DEFAULT_HEADER, id).post(ClientResponse.class);

		assertThat(MOCK_TEST.getRequestTrackerId(), equalTo(id.toString()));
	}

}
