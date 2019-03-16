package com.revenat.jmemcached.protocol;

import java.io.IOException;
import java.io.OutputStream;

import com.revenat.jmemcached.protocol.model.Request;

/**
 * Responsible for writing {@link Request} object into {@link OutputStream}
 * 
 * @author Vitaly Dragun
 *
 */
public interface RequestWriter {

	/**
	 * Writes specified {@link Request} object into specified {@code OutputStream}
	 * @throws IOException
	 */
	void writeTo(OutputStream output, Request request) throws IOException;
}
