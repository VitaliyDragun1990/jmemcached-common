package com.revenat.jmemcached.protocol.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.revenat.jmemcached.exception.JMemcachedException;

public class CommandTest {

	@Test
	public void shouldReturnCleanCommandForValueZero() throws Exception {
		assertThat(Command.valueOf((byte) 0), equalTo(Command.CLEAR));
	}

	@Test
	public void shouldReturnPutCommandForValueOne() throws Exception {
		assertThat(Command.valueOf((byte) 1), equalTo(Command.PUT));
	}
	
	@Test
	public void shouldReturnGetCommandForValueTwo() throws Exception {
		assertThat(Command.valueOf((byte) 2), equalTo(Command.GET));
	}
	
	@Test
	public void shouldReturnRemoveCommandForValueThree() throws Exception {
		assertThat(Command.valueOf((byte) 3), equalTo(Command.REMOVE));
	}
	
	@Test(expected = JMemcachedException.class)
	public void shouldNotAllowToGetCommandForInvalidByteValue() throws Exception {
		byte invalidByteCode = (byte) 10;
		Command.valueOf(invalidByteCode);
	}
	
	@Test
	public void shouldReturnByteValueZeroForClearCommand() throws Exception {
		assertThat(Command.CLEAR.getByteCode(), equalTo((byte) 0));
	}
	
	@Test
	public void shouldReturnByteValueOneForPutCommand() throws Exception {
		assertThat(Command.PUT.getByteCode(), equalTo((byte) 1));
	}
	
	@Test
	public void shouldReturnByteValueTwoForGetCommand() throws Exception {
		assertThat(Command.GET.getByteCode(), equalTo((byte) 2));
	}
	
	@Test
	public void shouldReturnByteValueThreeForRemoveCommand() throws Exception {
		assertThat(Command.REMOVE.getByteCode(), equalTo((byte) 3));
	}
}
