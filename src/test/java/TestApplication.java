import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class TestApplication extends Application<Configuration> {

	@Override
	public void initialize(Bootstrap<Configuration> bootstrap) {
		bootstrap.addBundle(new RequestTrackerBundle<Configuration>());
	}

	@Override
	public void run(Configuration configuration, Environment environment) throws Exception {		
		environment.jersey().register(new TestResource());
	}

}
