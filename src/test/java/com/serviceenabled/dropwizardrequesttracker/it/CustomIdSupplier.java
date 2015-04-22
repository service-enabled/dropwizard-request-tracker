package com.serviceenabled.dropwizardrequesttracker.it;

import com.google.common.base.Supplier;

public class CustomIdSupplier implements Supplier<String> {
	@Override
	public String get() {
		return "12345";
	}
}
