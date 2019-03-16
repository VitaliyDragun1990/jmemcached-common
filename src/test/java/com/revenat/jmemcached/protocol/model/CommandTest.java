package com.revenat.jmemcached.protocol.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.revenat.jmemcached.exception.JMemcachedException;

public class CommandTest {

	@Test
	public void returnsCleanCommandForValueZero() throws Exception {
		assertThat(Command.valueOf((byte) 0), equalTo(Command.CLEAR));
	}

	@Test
	public void returnsPutCommandForValueOne() throws Exception {
		assertThat(Command.valueOf((byte) 1), equalTo(Command.PUT));
	}
	
	@Test
	public void returnsGetCommandForValueTwo() throws Exception {
		assertThat(Command.valueOf((byte) 2), equalTo(Command.GET));
	}
	
	@Test
	public void returnsRemoveCommandForValueThree() throws Exception {
		assertThat(Command.valueOf((byte) 3), equalTo(Command.REMOVE));
	}
	
	@Test(expected = JMemcachedException.class)
	public void throwsJMemcachedExceptionIfNoCommandForGivenByteValue() throws Exception {
		byte invalidByteCode = (byte) 10;
		Command.valueOf(invalidByteCode);
	}
	
	@Test
	public void returnsByteValueZeroForClearCommand() throws Exception {
		assertThat(Command.CLEAR.getByteCode(), equalTo((byte) 0));
	}
	
	@Test
	public void returnsByteValueOneForPutCommand() throws Exception {
		assertThat(Command.PUT.getByteCode(), equalTo((byte) 1));
	}
	
	@Test
	public void returnsByteValueTwoForGetCommand() throws Exception {
		assertThat(Command.GET.getByteCode(), equalTo((byte) 2));
	}
	
	@Test
	public void returnsByteValueThreeForRemoveCommand() throws Exception {
		assertThat(Command.REMOVE.getByteCode(), equalTo((byte) 3));
	}
}
