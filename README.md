dropwizard-request-tracker
==========================

Track service requests through multiple hops using a UUID.

[![Build Status](https://travis-ci.org/service-enabled/dropwizard-request-tracker.svg?branch=master)](https://travis-ci.org/service-enabled/dropwizard-request-tracker)

Description
-----------

This project uses servlet filters, Jersey client filters, and [Logback's MDC](http://logback.qos.ch/manual/mdc.html) to keep track of an ID through multiple hops. The servlet and client filters will intercept a request and automatically generate an ID if none exists. The client filter adds the ID as a request header. The servlet filter intercepts the ID.

There's a Dropwizard bundle that will add in the servlet filter for you. Jersey client filters have to be manually added to your client classes.

Compatibility
-------------

| Version       | Dropwizard Version                   | Java Version  |
| ------------- |:-------------------------------------|--------------:|
| 3.0.x         | 1.3.x,                               | 8+            |
| 2.0.x         | 1.3.x, 1.2.x, 1.1.x, 1.0.x, 0.9.x    | 7+            |
| 0.2.x         | 0.8.x, 0.7.x                         | 7+            |

Integrating with existing dropwizard project
--------------------------------------------

Add the following dependency into your pom.xml

```xml
<dependency>
    <groupId>com.serviceenabled</groupId>
    <artifactId>dropwizard-request-tracker</artifactId>
    <version>3.0.0</version>
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

The `RequestTrackerConfiguration` sets the HTTP header name to `X-Request-Tracker` and the MDC key to `Request-Tracker` by default.  These can be overridden in your YAML configuration.

By default `UuidSupplier` is used by the bundle and filters.  The provided bundle and filters provide constructors for you to pass in your own custom ID supplier.  Your custom ID supplier must implement Guava's `Supplier<String>`.  Here's an example ID supplier:

```java
import com.google.common.base.Supplier;

public class CustomIdSupplier implements Supplier<String> {
	@Override
	public String get() {
		return "12345";
	}
}
```


License
-------

[MIT](https://github.com/service-enabled/dropwizard-request-tracker/blob/master/LICENSE)
