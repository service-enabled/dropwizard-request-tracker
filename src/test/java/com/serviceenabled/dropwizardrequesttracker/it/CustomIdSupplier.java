package com.serviceenabled.dropwizardrequesttracker.it;

import java.util.function.Supplier;

public class CustomIdSupplier implements Supplier<String> {
	@Override
	public String get() {
		return "12345";
	}
}
