package com.revenat.jmemcached.protocol.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.revenat.jmemcached.exception.JMemcachedException;

public class VersionTest {
	private static final byte VERSION_1_0 = (byte) 0b00010000;
	private static final byte VERSION_0_0 = (byte) 0b00000000;

	@Test
	public void returnsVersionOnePointZeroForByteValue16() throws Exception {
		Version onePointZero = Version.valueOf(VERSION_1_0);
		
		assertThat(onePointZero, equalTo(Version.VERSION_1_0));
	}
	
	@Test
	public void returnsVersionZeroPointZeroForByteValue0() throws Exception {
		Version onePointZero = Version.valueOf(VERSION_0_0);
		
		assertThat(onePointZero, equalTo(Version.VERSION_0_0));
	}
	
	@Test(expected = JMemcachedException.class)
	public void throwsJMemcachedExceptionIfNoVersionForByteValue() throws Exception {
		Version.valueOf((byte) 0b01111111);
	}
	
	@Test
	public void returnsByteValue16ForVersionOnePointZero() throws Exception {
		assertThat(Version.VERSION_1_0.getByteCode(), equalTo(VERSION_1_0));
	}
	
	@Test
	public void returnsByteValue0ForVersionZeroPointZero() throws Exception {
		assertThat(Version.VERSION_0_0.getByteCode(), equalTo(VERSION_0_0));
	}
	
	@Test
	public void returnsStringRepresentationOfTheVersion() throws Exception {
		String version = Version.VERSION_1_0.toString();
		
		assertThat(version, equalTo("1.0"));
	}
}
