package com.revenat.jmemcached.protocol.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

public class RequestTest {

	private static final long TTL = ZonedDateTime.now(ZoneId.systemDefault()).plusHours(1).toInstant().toEpochMilli();
	private static final byte[] DATA = new byte[] { 1, 2, 3 };
	private static final String KEY = "key";
	
	private Request request;

	@Test
	public void createsClearRequest() throws Exception {
		request = Request.clear();

		assertClear(request);
	}

	private static void assertClear(Request request) {
		assertThat(request.getCommand(), equalTo(Command.CLEAR));
		assertFalse("CLEAR request should not contain key", request.hasKey());
		assertFalse("CLEAR request should not contain ttl", request.hasTtl());
		assertFalse("CLEAR request should not contain any data", request.hasData());
	}

	@Test
	public void createsGetRequest() throws Exception {
		request = Request.get(KEY);

		assertGet(request);
	}

	private static void assertGet(Request request) {
		assertThat(request.getCommand(), equalTo(Command.GET));
		assertTrue("GET request should contain a key", request.hasKey());
		assertThat(request.getKey(), equalTo(KEY));
		assertFalse("GET request should not contain ttl", request.hasTtl());
		assertFalse("GET request should not contain any data", request.hasData());
	}

	@Test
	public void createsRemoveRequest() throws Exception {
		request = Request.remove(KEY);

		assertCreate(request);
	}

	private static void assertCreate(Request request) {
		assertThat(request.getCommand(), equalTo(Command.REMOVE));
		assertTrue("REMOVE request should contain a key", request.hasKey());
		assertThat(request.getKey(), equalTo(KEY));
		assertFalse("REMOVE request should not contain ttl", request.hasTtl());
		assertFalse("REMOVE request should not contain any data", request.hasData());
	}

	@Test
	public void createsPutRequestWithoutTtl() throws Exception {
		request = Request.put(KEY, DATA);

		assertPutWithoutTtl(request);
	}

	private static void assertPutWithoutTtl(Request request) {
		assertThat(request.getCommand(), equalTo(Command.PUT));
		assertTrue("PUT request should contain a key", request.hasKey());
		assertTrue("PUT request should contain a data", request.hasData());
		assertFalse("PUT request without ttl should not contain ttl", request.hasTtl());
		assertThat(request.getKey(), equalTo(KEY));
		assertThat(request.getData(), equalTo(DATA));
	}

	@Test
	public void createsPutRequestWithTtl() throws Exception {
		request = Request.put(KEY, DATA, TTL);

		assertPutWithTtl(request);
	}

	private static void assertPutWithTtl(Request request) {
		assertThat(request.getCommand(), equalTo(Command.PUT));
		assertTrue("PUT request should contain a key", request.hasKey());
		assertTrue("PUT request should contain a data", request.hasData());
		assertTrue("PUT request with ttl should contain ttl", request.hasTtl());
		assertThat(request.getKey(), equalTo(KEY));
		assertThat(request.getData(), equalTo(DATA));
		assertThat(request.getTtl(), equalTo(TTL));
	}

	@Test(expected = NullPointerException.class)
	public void throwsNullPointerExceptionIfCreatedWithNullKey() throws Exception {
		Request.get(null);
	}

	@Test
	public void containsNoDataIfCreatedWithNullData() throws Exception {
		request = Request.put(KEY, null);
		
		assertThat(request.hasData(), equalTo(false));
		assertThat(request.getData(), equalTo(new byte[0]));
	}

	@Test
	public void returnsStringRepresentationWithCommandName() throws Exception {
		request = Request.clear();

		String stringRequest = request.toString();

		assertThat(stringRequest, equalTo(Command.CLEAR.name()));
	}

	@Test
	public void returnsStringRepresentationWithKeyIfAny() throws Exception {
		request = Request.get(KEY);

		assertThat(request.toString(), containsString(KEY));
	}

	@Test
	public void returnsStringRepresentationWithDataLengthInBytesIfAny() throws Exception {
		request = Request.put(KEY, DATA);

		assertThat(request.toString(), containsString("=" + DATA.length + " bytes"));
	}

	@Test
	public void returnsStringRepresentationWithTtlIfAny() throws Exception {
		request = Request.put(KEY, DATA, TTL);

		assertThat(request.toString(), containsString(calculateTtl(TTL)));
	}
	
	@Test
	public void equalsToAnotherRequestWithEqualsCommandKeyAndTtl() throws Exception {
		Request requestA = Request.put(KEY, DATA, TTL);
		Request requestB = Request.put(KEY, DATA, TTL);
		
		assertEquals("Requests should be equal",requestA, requestB);
	}
	
	@Test
	public void equalRequestsHaveEqualHashCodes() throws Exception {
		Request requestA = Request.clear();
		Request requestB = Request.clear();
		
		assertThat(requestA.hashCode(), equalTo(requestB.hashCode()));
	}

	private static String calculateTtl(long ttl) {
		return String.format("time-to-live=%d milliseconds", ttl);
	}

}
