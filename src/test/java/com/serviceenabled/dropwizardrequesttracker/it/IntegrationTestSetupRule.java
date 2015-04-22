package com.serviceenabled.dropwizardrequesttracker.it;

import com.serviceenabled.dropwizardrequesttracker.RequestTrackerClientFilter;
import com.serviceenabled.dropwizardrequesttracker.RequestTrackerConfiguration;
import com.sun.jersey.api.client.Client;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.net.URI;

/**
 * Created by jon on 4/21/15.
 */
public class IntegrationTestSetupRule implements TestRule {
	private final DropwizardAppRule<BundleConfiguration> dropwizardAppRule;
	private final Client client = new Client();
	private ClientTestResource clientTestResource;
	private MockTestResource mockTestResource;
	private URI initialUri;
	private RequestTrackerConfiguration configuration;

	public IntegrationTestSetupRule(DropwizardAppRule<BundleConfiguration> dropwizardAppRule) {
		this.dropwizardAppRule = dropwizardAppRule;
	}

	@Override
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				configuration = dropwizardAppRule.getConfiguration().getRequestTrackerConfiguration();
				client.addFilter(new RequestTrackerClientFilter(configuration));

				initialUri = new URI("HTTP", null, "localhost", dropwizardAppRule.getLocalPort(), "/client-test", null, null);
				URI secondaryURI = new URI("HTTP", null, "localhost", dropwizardAppRule.getLocalPort(), "/mock-test", null, null);
				clientTestResource = new ClientTestResource(secondaryURI, client);
				mockTestResource = new MockTestResource();

				dropwizardAppRule.getEnvironment().getApplicationContext().stop();
				dropwizardAppRule.getEnvironment().jersey().register(mockTestResource);
				dropwizardAppRule.getEnvironment().jersey().register(clientTestResource);
				dropwizardAppRule.getEnvironment().getApplicationContext().start();

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
