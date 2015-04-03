package com.serviceenabled.dropwizardrequesttracker.supplier;

import com.google.common.base.Supplier;

public class SupplierFactory {
	private String implementationClass = "com.serviceenabled.dropwizardrequesttracker.supplier.UuidSupplier";

	public String getImplementationClass() {
		return implementationClass;
	}

	public void setImplementationClass(String implementationClass) {
		this.implementationClass = implementationClass;
	}

	@SuppressWarnings("unchecked")
	public Supplier<String> build() {
		try {
			return (Supplier<String>) Class.forName(this.implementationClass).getConstructor().newInstance();
		} 
		catch (Exception e) {
			throw new RuntimeException(String.format("could not create supplier from %s",this.implementationClass));
		}
	}
}
