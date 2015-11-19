package com.serviceenabled.dropwizardrequesttracker.it;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import io.dropwizard.setup.Environment;

import java.util.UUID;

import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.Entity;


public class BundleApplicationIT {
	private static final DropwizardAppRule<BundleConfiguration> DROPWIZARD_APP_RULE = new DropwizardAppRule<BundleConfiguration>(BundleApplication.class);
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
		client.target(INTEGRATION_TEST_SETUP_RULE.getInitialUri())
				.request()
				.post(Entity.json(null), ClientResponseContext.class);

		assertThat(INTEGRATION_TEST_SETUP_RULE.getMockTestResource().getRequestTrackerId(), notNullValue());
	}

	@Test
	public void keepsTheId() throws Exception {
		Client client = ClientBuilder.newClient();
		UUID id = UUID.randomUUID();
		client.target(INTEGRATION_TEST_SETUP_RULE.getInitialUri())
				.request()
				.header(INTEGRATION_TEST_SETUP_RULE.getConfiguration().getHeaderName(), id)
				.post(Entity.json(null),ClientResponseContext.class);

		assertThat(INTEGRATION_TEST_SETUP_RULE.getMockTestResource().getRequestTrackerId(), equalTo(id.toString()));
	}
}