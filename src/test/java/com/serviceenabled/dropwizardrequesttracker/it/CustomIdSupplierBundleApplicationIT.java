package com.serviceenabled.dropwizardrequesttracker.it;

import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.Entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class CustomIdSupplierBundleApplicationIT {
	private static final DropwizardAppRule<BundleConfiguration> DROPWIZARD_APP_RULE = new DropwizardAppRule<>(CustomIdSupplierBundleApplication.class);
	private static final IntegrationTestSetupRule INTEGRATION_TEST_SETUP_RULE = new IntegrationTestSetupRule(DROPWIZARD_APP_RULE);

	@ClassRule
	public static final RuleChain chain = RuleChain
			.outerRule(DROPWIZARD_APP_RULE)
			.around(INTEGRATION_TEST_SETUP_RULE);

	@Test
	public void suppliesCustomId() throws Exception {
		Client client = ClientBuilder.newClient();
		client.target(INTEGRATION_TEST_SETUP_RULE.getInitialUri()).request().post(Entity.json(null), ClientResponseContext.class);

		assertThat(INTEGRATION_TEST_SETUP_RULE.getMockTestResource().getRequestTrackerId(), equalTo("12345"));
	}
}