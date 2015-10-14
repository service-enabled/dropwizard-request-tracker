package com.serviceenabled.dropwizardrequesttracker;

public class RequestTrackerConfiguration {

	public RequestTrackerConfiguration() {}

	public RequestTrackerConfiguration(String headerName, String mdcKey) {
		setHeaderName(headerName);
		setMdcKey(mdcKey);
	}

	private String headerName = "X-Request-Tracker";
	private String mdcKey = "Request-Tracker";
	private Boolean addResponseHeader = false;

	public Boolean getAddResponseHeader() {
		return this.addResponseHeader;
	}

	public void setAddResponseHeader(Boolean val) {
		this.addResponseHeader = val;
	}

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
}
