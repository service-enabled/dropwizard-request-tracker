package com.serviceenabled.dropwizardrequesttracker.it;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class CustomIdSupplierBundleApplicationIT {
	private static final DropwizardAppRule<BundleConfiguration> DROPWIZARD_APP_RULE = new DropwizardAppRule<BundleConfiguration>(CustomIdSupplierBundleApplication.class, null);
	private static final IntegrationTestSetupRule INTEGRATION_TEST_SETUP_RULE = new IntegrationTestSetupRule(DROPWIZARD_APP_RULE);

	@ClassRule
	public static final RuleChain chain = RuleChain
			.outerRule(DROPWIZARD_APP_RULE)
			.around(INTEGRATION_TEST_SETUP_RULE);

	@Test
	public void suppliesCustomId() throws Exception {
		Client client = new Client();
		client.resource(INTEGRATION_TEST_SETUP_RULE.getInitialUri()).post(ClientResponse.class);

		assertThat(INTEGRATION_TEST_SETUP_RULE.getMockTestResource().getRequestTrackerId(), equalTo("12345"));
	}
}