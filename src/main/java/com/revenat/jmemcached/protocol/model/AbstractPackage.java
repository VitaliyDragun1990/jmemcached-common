package com.revenat.jmemcached.protocol.model;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;

/**
 * This component represent protocol's abstract package (frame) that holds
 * some kind of data as array of bytes.
 * 
 * @author Vitaly Dragun
 *
 */
abstract class AbstractPackage {
	private final byte[] data;

	AbstractPackage(byte[] data) {
		this.data = requireNonNull(data,  "data can not be null");
	}

	AbstractPackage() {
		this(new byte[0]);
	}

	public byte[] getData() {
		return Arrays.copyOf(data, data.length);
	}
	
	public boolean hasData() {
		return data.length > 0;
	}
}
