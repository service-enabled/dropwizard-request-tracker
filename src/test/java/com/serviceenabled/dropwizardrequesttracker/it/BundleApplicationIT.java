package com.serviceenabled.dropwizardrequesttracker.it;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import io.dropwizard.setup.Environment;

import java.util.UUID;

import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import org.junit.rules.RuleChain;


public class BundleApplicationIT {
	private static final DropwizardAppRule<BundleConfiguration> DROPWIZARD_APP_RULE = new DropwizardAppRule<BundleConfiguration>(BundleApplication.class, null);
	private static final IntegrationTestSetupRule INTEGRATION_TEST_SETUP_RULE = new IntegrationTestSetupRule(DROPWIZARD_APP_RULE);

	@ClassRule
	public static final RuleChain chain = RuleChain
			.outerRule(DROPWIZARD_APP_RULE)
			.around(INTEGRATION_TEST_SETUP_RULE);

	@Test
	public void bundleAddsFilter(){
		Environment e = INTEGRATION_TEST_SETUP_RULE.getEnvironment();

		assertThat(e.getApplicationContext().getServletHandler().getFilter("request-tracker-servlet-filter"), notNullValue());
	}

	@Test
	public void addsTrackerToOutgoingRequest() throws Exception {
		Client client = INTEGRATION_TEST_SETUP_RULE.getClient();
		client.resource(INTEGRATION_TEST_SETUP_RULE.getInitialUri())
				.post(ClientResponse.class);

		assertThat(INTEGRATION_TEST_SETUP_RULE.getMockTestResource().getRequestTrackerId(), notNullValue());
	}

	@Test
	public void keepsTheId() throws Exception {
		Client client = new Client();
		UUID id = UUID.randomUUID();
		client.resource(INTEGRATION_TEST_SETUP_RULE.getInitialUri())
				.header(INTEGRATION_TEST_SETUP_RULE.getConfiguration().getHeaderName(), id)
				.post(ClientResponse.class);

		assertThat(INTEGRATION_TEST_SETUP_RULE.getMockTestResource().getRequestTrackerId(), equalTo(id.toString()));
	}
}