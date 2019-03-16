package com.revenat.jmemcached.exception;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;

public class JMemcachedConfigExceptionTest {
	private static final Throwable CAUSE = new RuntimeException();
	private static final String MESSAGE = "Some message";
	
	private JMemcachedConfigException exception;
	
	@Test
	public void createsNewInstanceUsingMessage() throws Exception {
		exception = createWithMessage(MESSAGE);
		
		assertNotNull("Exception should not be null", exception);
		assertThat(exception.getMessage(), equalTo(MESSAGE));
	}

	
	@Test
	public void createsNewInstanceUsingMessageAndCause() throws Exception {
		exception = createWithMessageAndCause(MESSAGE, CAUSE);
		
		assertNotNull("Exception should not be null", exception);
		assertThat(exception.getMessage(), equalTo(MESSAGE));
		assertThat(exception.getCause(), equalTo(CAUSE));
	}


	private static JMemcachedConfigException createWithMessageAndCause(String message, Throwable cause) {
		return new JMemcachedConfigException(message, cause);
	}

	private static JMemcachedConfigException createWithMessage(String message) {
		return new JMemcachedConfigException(message);
	}
}
