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
    <version>0.1.0</version>
</dependency>
```

and `mvn clean install`

Examples
--------

### Use default HTTP header

The default HTTP header is `X-Request-Tracker` and the default MDC key is `Request-Tracker`

```

bootstrap.addBundle(new RequestTrackerBundle<Configuration>());

```

```

client.addFilter(new RequestTrackerClientFilter());

```

### Customize the HTTP Header

The HTTP header can be customized through the `RequestTrackerBundle` constructor.

```

bootstrap.addBundle(new RequestTrackerBundle<Configuration>("foo"));

```


```

client.addFilter(new RequestTrackerClientFilter("foo"));

```

License
-------

[MIT](https://github.com/service-enabled/dropwizard-request-tracker/blob/master/LICENSE)
