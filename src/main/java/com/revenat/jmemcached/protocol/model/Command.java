package com.revenat.jmemcached.protocol.model;

import com.revenat.jmemcached.exception.JMemcachedException;

/**
 * Represents protocol's request command.
 * 
 * @author Vitaly Dragun
 *
 */
public enum Command {
	CLEAR(0),
	
	PUT(1),
	
	GET(2),
	
	REMOVE(3);

	private byte code;

	private Command(int code) {
		this.code = (byte) code;
	}

	/**
	 * Returns {@code byte} value representing this command.
	 */
	public byte getByteCode() {
		return code;
	}

	/**
	 * Returns {@link Command} instance that corresponds with provided
	 * {@code byteCode} value.
	 * 
	 * @param byteCode byte value that represents some {@link Command}
	 * @throws JMemcachedException if no {@link Command} that corresponds with
	 *                             provided {@code byteCode} value
	 */
	public static Command valueOf(byte byteCode) {
		for (Command command : values()) {
			if (command.getByteCode() == byteCode) {
				return command;
			}
		}
		throw new JMemcachedException("Unsuported byteCode for Command: " + byteCode);
	}
}
