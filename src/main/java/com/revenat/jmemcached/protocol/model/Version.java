package com.revenat.jmemcached.protocol.model;

import com.revenat.jmemcached.exception.JMemcachedException;

/**
 * Represents protocol version. Can be represented as {@code 1 byte} where 3
 * highest bits (first one is excepted because it represents sign in java byte)
 * represent version's MAJOR part, and 4 bits that remain represent version's
 * MINOR part. These restrictions means that maximum version number can be
 * {@code 7.15} ({@code 01111111} in binary representation), and minimum one
 * {@code 0.0} ({@code 00000000} in binary representation).
 * 
 * @author Vitaly Dragun
 *
 */
public enum Version {
	VERSION_0_0(0, 0),
	
	VERSION_1_0(1, 0);

	/**
	 * Stores 3 bits which represent version's MAJOR part
	 */
	private byte high;
	/**
	 * Stores 4 bits which represent version's MINOR part
	 */
	private byte low;

	private Version(int high, int low) {
		this.high = (byte) (high & 0x7); // retains only 3 lower bits of the byte
		this.low = (byte) (low & 0xF); // retains only 4 lower bits of the byte
	}

	/**
	 * Returns {@link Version} instance that corresponds with provided
	 * {@code buteCode} value
	 * 
	 * @param byteCode byte value that represents some {@link Version}
	 * @throws JMemcachedException if no {@link Version} that corresponds with
	 *                             provided {@code byteCode} value
	 */
	public static Version valueOf(byte byteCode) {
		for (Version version : values()) {
			if (version.getByteCode() == byteCode) {
				return version;
			}
		}
		throw new JMemcachedException("Unsupported byteCode for Version: " + byteCode);
	}

	/**
	 * Returns {@code byte} representing this {@link Version}
	 */
	public byte getByteCode() {
		return (byte) (low + (high << 4));
	}

	@Override
	public String toString() {
		return String.format("%s.%s", high, low);
	}
}
