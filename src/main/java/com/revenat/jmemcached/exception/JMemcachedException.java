package com.revenat.jmemcached.exception;

/**
 * Basic class for all application-specific exceptions
 * related to {@code JMemcached} application.
 * 
 * @author Vitaly Dragun
 *
 */
public class JMemcachedException extends RuntimeException {
	private static final long serialVersionUID = -9129448714853292023L;

	public JMemcachedException(String message, Throwable cause) {
		super(message, cause);
	}

	public JMemcachedException(String message) {
		super(message);
	}

	public JMemcachedException(Throwable cause) {
		super(cause);
	}

}
