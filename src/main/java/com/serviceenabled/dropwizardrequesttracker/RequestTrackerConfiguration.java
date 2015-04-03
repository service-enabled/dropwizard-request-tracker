package com.serviceenabled.dropwizardrequesttracker;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.serviceenabled.dropwizardrequesttracker.supplier.SupplierFactory;

public class RequestTrackerConfiguration {

	public RequestTrackerConfiguration() {}
	
	public RequestTrackerConfiguration(String headerName, String mdcKey) {
		setHeaderName(headerName);
		setMdcKey(mdcKey);
	}

	@JsonProperty("supplier")
	private SupplierFactory supplierFactory = new SupplierFactory();
	
	private String headerName = "X-Request-Tracker";
	private String mdcKey = "Request-Tracker";

	public String getHeaderName() {
		return headerName;
	}
	
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	
	public String getMdcKey() {
		return mdcKey;
	}
	
	public void setMdcKey(String mdcKey) {
		this.mdcKey = mdcKey;
	}

	public SupplierFactory getSupplierFactory() {
		return supplierFactory;
	}

	public void setSupplierFactory(SupplierFactory supplierFactory) {
		this.supplierFactory = supplierFactory;
	}
}