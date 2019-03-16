package com.revenat.jmemcached.protocol;

import java.io.IOException;
import java.io.OutputStream;

import com.revenat.jmemcached.protocol.model.Response;

/**
 * Responsible for writing response into {@link OutputStream}
 * 
 * @author Vitaly Dragun
 *
 */
public interface ResponseWriter {

	/**
	 * Writes specified {@link Response} object into specified {@link OutputStream}
	 * @throws IOException
	 */
	void writeTo(OutputStream output, Response response) throws IOException;
}
