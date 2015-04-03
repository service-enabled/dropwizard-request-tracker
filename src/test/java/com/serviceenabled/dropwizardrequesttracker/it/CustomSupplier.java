package com.serviceenabled.dropwizardrequesttracker.it;

import com.google.common.base.Supplier;

public class CustomSupplier implements Supplier<String> {

	public String get() {
		return "SUPPLY-WHATEVER-YOU-LIKE";
	}
}