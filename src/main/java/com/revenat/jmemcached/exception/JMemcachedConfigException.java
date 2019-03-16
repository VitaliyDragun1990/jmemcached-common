/**
 * 
 */
package com.revenat.jmemcached.exception;

/**
 * Exception that signifies some kind of error
 * related to the configuration process.
 * 
 * @author Vitaly Dragun
 *
 */
public class JMemcachedConfigException extends JMemcachedException {
	private static final long serialVersionUID = -2724132273261207439L;

	public JMemcachedConfigException(String message, Throwable cause) {
		super(message, cause);
	}

	public JMemcachedConfigException(String message) {
		super(message);
	}

}
