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
	public void shouldAllowToCreateWithCommandOnly() throws Exception {
		request = Request.empty(Command.CLEAR);

		assertRequestWithCommandOnly(request);
	}

	private static void assertRequestWithCommandOnly(Request request) {
		assertThat(request.getCommand(), notNullValue());
		assertFalse("Empty request should not contain key", request.hasKey());
		assertFalse("Empty request should not contain ttl", request.hasTtl());
		assertFalse("Empty request should not contain any data", request.hasData());
	}

	@Test
	public void shouldAllowToCreateWithCommandAndKey() throws Exception {
		request = Request.withKey(Command.GET, KEY);

		assertRequestWithCommandAndKey(request);
	}

	private static void assertRequestWithCommandAndKey(Request request) {
		assertThat(request.getCommand(), notNullValue());
		assertTrue("Requst with only command and key should contain a key", request.hasKey());
		assertThat(request.getKey(), notNullValue());
		assertFalse("Requst with only command and key should not contain ttl", request.hasTtl());
		assertFalse("Requst with only command and key should not contain any data", request.hasData());
	}

	@Test
	public void shouldAllowToCreateRequestWithCommandAndKeyAndData() throws Exception {
		request = Request.withKeyAndData(Command.PUT, KEY, DATA, null);

		assertRequestWithCommandAndKeyAndData(request);
	}

	private static void assertRequestWithCommandAndKeyAndData(Request request) {
		assertThat(request.getCommand(), notNullValue());
		assertTrue("Request with command, key and data should contain a key", request.hasKey());
		assertTrue("Request with command, key and data should contain a data", request.hasData());
		assertThat(request.getKey(), notNullValue());
		assertThat(request.getData(), notNullValue());
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldNotAllowToCreateEmptyRequestWithNullCommand() throws Exception {
		Request.empty(null);
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldNotAllowToCreateCommandAndKeyRequestWithNullCommand() throws Exception {
		Request.withKey(null, KEY);
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldNotAllowToCreateCommandAndKeyRequestWithNullKey() throws Exception {
		Request.withKey(Command.GET, null);
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldNotAllowToCreateCommandKeyDataRequestWithNullCommand() throws Exception {
		Request.withKeyAndData(null, KEY, DATA, 1000L);
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldNotAllowToCreateCommandKeyDataRequestWithNullKey() throws Exception {
		Request.withKeyAndData(Command.PUT, null, DATA, 1000L);
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldNotAllowToCreateCommandKeyDataRequestWithNullData() throws Exception {
		Request.withKeyAndData(Command.PUT, KEY, null, 1000L);
	}


	@Test
	public void shouldReturnStringnWithCommandName() throws Exception {
		request = Request.empty(Command.CLEAR);

		String stringRequest = request.toString();

		assertThat(stringRequest, containsString(Command.CLEAR.name()));
	}

	@Test
	public void shouldReturnStringWithKeyIfAny() throws Exception {
		request = Request.withKey(Command.GET, KEY);

		assertThat(request.toString(), containsString(KEY));
	}

	@Test
	public void shouldReturnStringWithDataLengthInBytesIfAny() throws Exception {
		request = Request.withKeyAndData(Command.PUT, KEY, DATA, null);

		assertThat(request.toString(), containsString(DATA.length + " bytes"));
	}

	@Test
	public void shouldReturnStringWithTtlIfAny() throws Exception {
		request = Request.withKeyAndData(Command.PUT, KEY, DATA, TTL);

		assertThat(request.toString(), containsString(calculateTtl(TTL)));
	}

	private static String calculateTtl(long ttl) {
		return String.format("time-to-live=%d milliseconds", ttl);
	}

}
