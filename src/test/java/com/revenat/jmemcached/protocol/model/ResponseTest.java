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
	
	@Test
	public void containsNoDataIfCreatedWithNullData() throws Exception {
		response = Response.withData(Status.GOTTEN, null);
		
		assertThat(response.hasData(), is(false));
		assertThat(response.getData(), equalTo(new byte[0]));
	}
	
	@Test
	public void returnsStringRepresentationWithStatusName() throws Exception {
		response = Response.empty(Status.CLEARED);
		
		assertThat(response.toString(), containsString(response.getStatus().name()));
	}
	
	@Test
	public void stringRepresentationContainsDataLengthIfAny() throws Exception {
		response = Response.withData(Status.GOTTEN, DATA);
		
		assertThat(response.toString(), containsString(String.format("%s [%d bytes]",
				response.getStatus().name(), DATA.length)));
	}
	
	@Test
	public void equalsToAnotherResponseWithSameDataAndStatus() throws Exception {
		Response responseA = Response.withData(Status.GOTTEN, DATA);
		Response responseB = Response.withData(Status.GOTTEN, DATA);
		
		assertEquals("Responses should be equal", responseA, responseB);
	}
	
	@Test
	public void notEqualsToAnotherResponseWithSameStatusButDIfferentData() throws Exception {
		Response responseA = Response.withData(Status.GOTTEN, DATA);
		Response responseB = Response.withData(Status.GOTTEN, null);
		
		assertNotEquals("Responses should not be equal", responseA, responseB);
	}
	
	@Test
	public void notEqualsToAnotherResponseWithSametDataButDifferentStatus() throws Exception {
		Response responseA = Response.withData(Status.GOTTEN, DATA);
		Response responseB = Response.withData(Status.REPLACED, DATA);
		
		assertNotEquals("Responses should not be equal", responseA, responseB);
	}
	
	@Test
	public void equalResponsesHaveEqualHashCodes() throws Exception {
		Response responseA = Response.withData(Status.GOTTEN, DATA);
		Response responseB = Response.withData(Status.GOTTEN, DATA);
		
		assertThat(responseA.hashCode(), equalTo(responseB.hashCode()));
	}
}
