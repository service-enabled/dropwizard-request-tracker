package com.serviceenabled.dropwizardrequesttracker.it;

import com.serviceenabled.dropwizardrequesttracker.RequestTrackerClientFilter;
import com.serviceenabled.dropwizardrequesttracker.RequestTrackerConfiguration;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.URI;

/**
 * Created by jon on 4/21/15.
 */
public class IntegrationTestSetupRule implements TestRule {
	private final DropwizardAppRule<BundleConfiguration> dropwizardAppRule;
	private final Client client = ClientBuilder.newClient();
	private ClientTestResource clientTestResource;
	private MockTestResource mockTestResource;
	private URI initialUri;
	private RequestTrackerConfiguration configuration;

	public IntegrationTestSetupRule(final DropwizardAppRule<BundleConfiguration> dropwizardAppRule) {
		this.dropwizardAppRule = dropwizardAppRule;

		dropwizardAppRule.addListener(new DropwizardAppRule.ServiceListener<BundleConfiguration>() {
			@Override
			public void onRun(BundleConfiguration config, Environment environment, DropwizardAppRule<BundleConfiguration> rule) throws Exception {
				clientTestResource = new ClientTestResource();
				mockTestResource = new MockTestResource();
				environment.jersey().register(mockTestResource);
				environment.jersey().register(clientTestResource);
			}

			@Override
			public void onStop(DropwizardAppRule<BundleConfiguration> rule) throws Exception {
				super.onStop(rule);
			}
		});
	}

	@Override
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				configuration = dropwizardAppRule.getConfiguration().getRequestTrackerConfiguration();
				client.register(new RequestTrackerClientFilter(configuration));

				initialUri = new URI("HTTP", null, "localhost", dropwizardAppRule.getLocalPort(), "/client-test", null, null);
				URI secondaryURI = new URI("HTTP", null, "localhost", dropwizardAppRule.getLocalPort(), "/mock-test", null, null);
				clientTestResource.setClient(client);
				clientTestResource.setUri(secondaryURI);

				base.evaluate();
			}
		};
	}

	public Environment getEnvironment() {
		return dropwizardAppRule.getEnvironment();
	}

	public Client getClient() {
		return client;
	}

	public URI getInitialUri() {
		return initialUri;
	}

	public MockTestResource getMockTestResource() {
		return mockTestResource;
	}

	public RequestTrackerConfiguration getConfiguration() {
		return dropwizardAppRule.getConfiguration().getRequestTrackerConfiguration();
	}
}
