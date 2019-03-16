package com.revenat.jmemcached.protocol.model;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.LocalDateTime;
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

		assertThat(request.getCommand(), equalTo(Command.CLEAR));
		assertFalse("CLEAR request should not contain key", request.hasKey());
		assertFalse("CLEAR request should not contain ttl", request.hasTtl());
		assertFalse("CLEAR request should not contain any data", request.hasData());
	}

	@Test
	public void createsGetRequest() throws Exception {
		request = Request.get(KEY);

		assertThat(request.getCommand(), equalTo(Command.GET));
		assertTrue("GET request should contain a key", request.hasKey());
		assertThat(request.getKey(), equalTo(KEY));
		assertFalse("GET request should not contain ttl", request.hasTtl());
		assertFalse("GET request should not contain any data", request.hasData());
	}

	@Test
	public void createsRemoveRequest() throws Exception {
		request = Request.remove(KEY);

		assertThat(request.getCommand(), equalTo(Command.REMOVE));
		assertTrue("REMOVE request should contain a key", request.hasKey());
		assertThat(request.getKey(), equalTo(KEY));
		assertFalse("REMOVE request should not contain ttl", request.hasTtl());
		assertFalse("REMOVE request should not contain any data", request.hasData());
	}

	@Test
	public void createsPutRequestWithoutTtl() throws Exception {
		request = Request.put(KEY, DATA);

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

	@Test(expected = NullPointerException.class)
	public void throwsNullPointerExceptionIfCreatedWithNullData() throws Exception {
		Request.put(KEY, null);
	}

	@Test
	public void returnsStringrepresentationWithCommandName() throws Exception {
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

		assertThat(request.toString(), containsString(calculateDateUntilDataWillBeStored(TTL)));
	}

	private static String calculateDateUntilDataWillBeStored(long ttl) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(ttl), ZoneId.systemDefault()).toString();
	}

}
