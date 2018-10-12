package com.serviceenabled.dropwizardrequesttracker;

import org.junit.Test;

import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.junit.Assert.*;

public class UuidSupplierTest {
	@Test
	public void suppliesAnId() {
		UuidSupplier uuidSupplier = new UuidSupplier();
		String id = uuidSupplier.get();

		assertThat(id, not(isEmptyOrNullString()));
	}
}