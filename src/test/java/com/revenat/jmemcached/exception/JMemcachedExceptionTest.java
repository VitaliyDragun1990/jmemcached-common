package com.revenat.jmemcached.exception;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class JMemcachedExceptionTest {
	private static final Throwable CAUSE = new RuntimeException();
	private static final String MESSAGE = "Some message";
	
	private JMemcachedException exception;

	@Test
	public void createsNewInstanceUsingCause() throws Exception {
		exception = new JMemcachedException(CAUSE);
		
		assertNotNull("Exception should not be null", exception);
		assertThat(exception.getCause(), equalTo(CAUSE));
	}
	
	@Test
	public void createsNewInstanceUsingMessage() throws Exception {
		exception = new JMemcachedException(MESSAGE);
		
		assertNotNull("Exception should not be null", exception);
		assertThat(exception.getMessage(), equalTo(MESSAGE));
	}
	
	@Test
	public void createsNewInstanceUsingMessageAndCause() throws Exception {
		exception = new JMemcachedException(MESSAGE, CAUSE);
		
		assertNotNull("Exception should not be null", exception);
		assertThat(exception.getMessage(), equalTo(MESSAGE));
		assertThat(exception.getCause(), equalTo(CAUSE));
	}

}
