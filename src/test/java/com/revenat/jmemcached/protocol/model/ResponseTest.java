package com.revenat.jmemcached.protocol.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ResponseTest {

	private static final byte[] DATA = new byte[] { 1, 2, 3 };
	private Response response;

	@Test
	public void shouldAllowToCreateEmptyResponse() throws Exception {
		response = Response.empty(Status.CLEARED);

		assertThat(response.getStatus(), equalTo(Status.CLEARED));
		assertFalse("Empty response should not contain data", response.hasData());
	}

	@Test
	public void shouldAllowToCreateResponseWithData() throws Exception {
		response = Response.withData(Status.GOTTEN, DATA);

		assertTrue("Response with data should contain data", response.hasData());
		assertThat(response.getData(), equalTo(DATA));
	}

	@Test(expected = NullPointerException.class)
	public void shouldNotAllowToCreateEmptyResponseWithNullStatus() throws Exception {
		Response.empty(null);
	}

	@Test(expected = NullPointerException.class)
	public void shouldNotAllowToCreateResponseWithDataUsingNullStatus() throws Exception {
		Response.withData(null, DATA);
	}

	@Test(expected = NullPointerException.class)
	public void shouldNotAllowToCreateResponseWithDataUsingNullData() throws Exception {
		Response.withData(Status.GOTTEN, null);
	}

	@Test
	public void emptyResponseShouldNotContainData() throws Exception {
		response = Response.empty(Status.CLEARED);

		assertThat(response.hasData(), is(false));
		assertThat(response.getData(), equalTo(new byte[0]));
	}

	@Test
	public void shouldReturnStringWithStatusName() throws Exception {
		response = Response.empty(Status.CLEARED);

		assertThat(response.toString(), containsString(response.getStatus().name()));
	}

	@Test
	public void shouldReturnStringnWithDataLengthIfAny() throws Exception {
		response = Response.withData(Status.GOTTEN, DATA);

		assertThat(response.toString(),
				containsString(String.format("%d bytes", DATA.length)));
	}
}
