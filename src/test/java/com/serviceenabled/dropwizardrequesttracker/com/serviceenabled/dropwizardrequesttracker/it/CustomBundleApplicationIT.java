package com.serviceenabled.dropwizardrequesttracker.com.serviceenabled.dropwizardrequesttracker.it;

import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;


public class CustomBundleApplicationIT {
	@ClassRule
	public static final DropwizardAppRule<Configuration> RULE = new DropwizardAppRule<Configuration>(CustomBundleApplication.class,null);
	
	@Test
	public void bundleAddsFilter(){
		Environment e = RULE.getEnvironment();
		assertThat(e.getApplicationContext().getServletHandler().getFilter("request-tracker-servlet-filter"),notNullValue());
	}
}
