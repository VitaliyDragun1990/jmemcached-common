package com.revenat.jmemcached.protocol.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ResponseTest {
	
	private static final byte[] DATA = new byte[] {1, 2, 3};
	private Response response;

	@Test
	public void createsEmptyResponseWithoutData() throws Exception {
		response = Response.empty(Status.CLEARED);
		
		assertThat(response.getStatus(), equalTo(Status.CLEARED));
		assertFalse("Empty response should not contain data", response.hasData());
	}
	
	@Test
	public void createsResponseWithData() throws Exception {
		response = Response.withData(Status.GOTTEN, DATA);
		
		assertTrue("Response with data should contain data", response.hasData());
		assertThat(response.getData(), equalTo(DATA));
	}
	
	@Test(expected = NullPointerException.class)
	public void throwsNullPointerExceptionIfCreatedWithNullStatus() throws Exception {
		Response.empty(null);
	}
	
	@Test(expected = NullPointerException.class)
	public void throwsNullPointerExceptionIfCreatedWithNullData() throws Exception {
		Response.withData(Status.GOTTEN, null);
	}

}
