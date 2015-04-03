dropwizard-request-tracker
==========================

Track service requests through multiple hops using a UUID.

[![Build Status](https://travis-ci.org/service-enabled/dropwizard-request-tracker.svg?branch=master)](https://travis-ci.org/service-enabled/dropwizard-request-tracker)

Description
-----------

This project uses servlet filters, Jersey client filters, and [Logback's MDC](http://logback.qos.ch/manual/mdc.html) to keep track of an ID through multiple hops. The servlet and client filters will intercept a request and automatically generate an ID if none exists. The client filter adds the ID as a request header. The servlet filter intercepts the ID.

There's a Dropwizard bundle that will add in the servlet filter for you. Jersey client filters have to be manually added to your client classes.


Integrating with existing dropwizard project
--------------------------------------------

Add the following dependency into your pom.xml

```xml
<dependency>
    <groupId>com.serviceenabled</groupId>
    <artifactId>dropwizard-request-tracker</artifactId>
    <version>0.2.0</version>
</dependency>
```

Add `RequestTrackerConfiguration` into your application's configuration class.

```java
public class ExampleConfiguration extends Configuration {

    private RequestTrackerConfiguration requestTrackerConfiguration = new RequestTrackerConfiguration();

    public RequestTrackerConfiguration getRequestTrackerConfiguration() {
        return requestTrackerConfiguration;
    }

    public void setRequestTrackerConfiguration(RequestTrackerConfiguration configuration) {
        this.requestTrackerConfiguration = configuration;
    }
}
```

Add the `RequestTrackerBundle` to your application

```java
bootstrap.addBundle(new RequestTrackerBundle<ExampleConfiguration>() {
    @Override
    public RequestTrackerConfiguration getRequestTrackerConfiguration(BundleConfiguration configuration) {
        return configuration.getRequestTrackerConfiguration();
    }
});
```

Add the client filter to your client classes

```java
client.addFilter(new RequestTrackerClientFilter(configuration.getRequestTrackerConfiguration()));
```

If you'd like the ID to be output in your logs, add `%X{Request-Tracker}` to your [logFormat](https://dropwizard.github.io/dropwizard/manual/configuration.html#logging).

and `mvn clean install`


Defaults
--------

The `RequestTrackerConfiguration` sets the HTTP header name to `X-Request-Tracker` and the MDC key to `Request-Tracker` by default.  These can be overridden in your YAML configuration.  The default ID generated is a UUID.  If you would like something other than a UUID, you can provide your own implementation class in the configuration.

Example YAML
-------
```yaml
requestTracker:
  headerName: Custom Header
  mdcKey: Custom Key
  supplier:
    implementationClass: com.your.package.CustomSupplier
```

License
-------

[MIT](https://github.com/service-enabled/dropwizard-request-tracker/blob/master/LICENSE)
