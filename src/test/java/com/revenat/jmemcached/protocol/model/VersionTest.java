package com.revenat.jmemcached.protocol.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.revenat.jmemcached.exception.JMemcachedException;

public class VersionTest {
	private static final byte BYTE_16 = (byte) 0b00010000;
	private static final byte BYTE_0 = (byte) 0b00000000;

	@Test
	public void shouldReturnVersionOnePointZeroForByteValue16() throws Exception {
		Version onePointZero = Version.valueOf(BYTE_16);
		
		assertThat(onePointZero, equalTo(Version.VERSION_1_0));
	}
	
	@Test
	public void shouldReturnVersionZeroPointZeroForByteValue0() throws Exception {
		Version onePointZero = Version.valueOf(BYTE_0);
		
		assertThat(onePointZero, equalTo(Version.VERSION_0_0));
	}
	
	@Test(expected = JMemcachedException.class)
	public void shouldNotAllowToGetVersionForInvalidValue() throws Exception {
		byte unsupportedByteValue = (byte) 0b01111111;
		Version.valueOf(unsupportedByteValue);
	}
	
	@Test
	public void shouldReturnByteValue16ForVersionOnePointZero() throws Exception {
		assertThat(Version.VERSION_1_0.getByteCode(), equalTo(BYTE_16));
	}
	
	@Test
	public void shouldReturnByteValue0ForVersionZeroPointZero() throws Exception {
		assertThat(Version.VERSION_0_0.getByteCode(), equalTo(BYTE_0));
	}
	
	@Test
	public void shouldReturnStringRepresentationOfTheVersion() throws Exception {
		String version = Version.VERSION_1_0.toString();
		
		assertThat(version, equalTo("1.0"));
	}
}
