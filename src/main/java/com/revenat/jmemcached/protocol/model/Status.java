package com.revenat.jmemcached.protocol.model;

import com.revenat.jmemcached.exception.JMemcachedException;

/**
 * Represents protocol's command status
 * 
 * @author Vitaly Dragun
 *
 */
public enum Status {
	ADDED(0),
	
	REPLACED(1),
	
	GOTTEN(2),
	
	NOT_FOUND(3),
	
	REMOVED(4),
	
	CLEARED(5)
	;

	private byte code;

	Status(int code) {
		this.code = (byte) code;
	}
	
	/**
	 * Returns {@link Status} instance that corresponds with provided
	 * {@code byteCode} value.
	 * 
	 * @param byteCode byte value that represents some {@link Status}
	 * @throws JMemcachedException if no {@link Status} that corresponds with
	 *                             provided {@code byteCode} value
	 */
	public static Status valueOf(byte byteCode) {
		for (Status status : values()) {
			if (status.getByteCode() == byteCode) {
				return status;
			}
		}
		throw new JMemcachedException("Unsupported byteCode for Status: " + byteCode);
	}
	
	/**
	 * Returns {@code byte} value representing this status.
	 */
	public byte getByteCode() {
		return code;
	}

}
