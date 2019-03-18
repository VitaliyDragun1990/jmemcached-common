package com.revenat.jmemcached.protocol.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.revenat.jmemcached.exception.JMemcachedException;

public class VersionTest {
	private static final byte BYTE_16 = (byte) 0b00010000;
	private static final byte BYTE_0 = (byte) 0b00000000;

	@Test
	public void returnsVersionOnePointZeroForByteValue16() throws Exception {
		Version onePointZero = Version.valueOf(BYTE_16);
		
		assertThat(onePointZero, equalTo(Version.VERSION_1_0));
	}
	
	@Test
	public void returnsVersionZeroPointZeroForByteValue0() throws Exception {
		Version onePointZero = Version.valueOf(BYTE_0);
		
		assertThat(onePointZero, equalTo(Version.VERSION_0_0));
	}
	
	@Test(expected = JMemcachedException.class)
	public void throwsJMemcachedExceptionIfNoVersionForByteValue() throws Exception {
		byte unsupportedByteValue = (byte) 0b01111111;
		Version.valueOf(unsupportedByteValue);
	}
	
	@Test
	public void returnsByteValue16ForVersionOnePointZero() throws Exception {
		assertThat(Version.VERSION_1_0.getByteCode(), equalTo(BYTE_16));
	}
	
	@Test
	public void returnsByteValue0ForVersionZeroPointZero() throws Exception {
		assertThat(Version.VERSION_0_0.getByteCode(), equalTo(BYTE_0));
	}
	
	@Test
	public void returnsStringRepresentationOfTheVersion() throws Exception {
		String version = Version.VERSION_1_0.toString();
		
		assertThat(version, equalTo("1.0"));
	}
}
