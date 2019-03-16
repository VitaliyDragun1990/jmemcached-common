package com.revenat.jmemcached.protocol.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.revenat.jmemcached.exception.JMemcachedException;

public class StatusTest {

	@Test
	public void returnsStatusAddedForValueZero() throws Exception {
		assertThat(Status.valueOf((byte) 0), equalTo(Status.ADDED));
	}
	
	@Test
	public void returnsStatusReplacedForValueOne() throws Exception {
		assertThat(Status.valueOf((byte) 1), equalTo(Status.REPLACED));
	}
	
	@Test
	public void returnsStatusGottenForValueTwo() throws Exception {
		assertThat(Status.valueOf((byte) 2), equalTo(Status.GOTTEN));
	}
	
	@Test
	public void returnsStatusNotFoundForValueThree() throws Exception {
		assertThat(Status.valueOf((byte) 3), equalTo(Status.NOT_FOUND));
	}
	
	@Test
	public void returnsStatusRemovedForValueFour() throws Exception {
		assertThat(Status.valueOf((byte) 4), equalTo(Status.REMOVED));
	}

	@Test
	public void returnsStatusClearedForValueFive() throws Exception {
		assertThat(Status.valueOf((byte) 5), equalTo(Status.CLEARED));
	}
	
	@Test(expected = JMemcachedException.class)
	public void throwsJMemcachedExceptionForInvalidValue() throws Exception {
		byte invalidByteCode = (byte) 10;
		
		Status.valueOf(invalidByteCode);
	}
	
	@Test
	public void returnsByteValueZeroForStatusAdded() throws Exception {
		assertThat(Status.ADDED.getByteCode(), equalTo((byte) 0));
	}
	
	@Test
	public void returnsByteValueOneForStatusReplaced() throws Exception {
		assertThat(Status.REPLACED.getByteCode(), equalTo((byte) 1));
	}
	
	@Test
	public void returnsByteValueTwoForStatusGotten() throws Exception {
		assertThat(Status.GOTTEN.getByteCode(), equalTo((byte) 2));
	}
	
	@Test
	public void returnsByteValueThreeForStatusNotFound() throws Exception {
		assertThat(Status.NOT_FOUND.getByteCode(), equalTo((byte) 3));
	}
	
	@Test
	public void returnsByteValueFourForStatusRemoved() throws Exception {
		assertThat(Status.REMOVED.getByteCode(), equalTo((byte) 4));
	}
	
	@Test
	public void returnsByteValueFiveForStatusCleared() throws Exception {
		assertThat(Status.CLEARED.getByteCode(), equalTo((byte) 5));
	}
}
