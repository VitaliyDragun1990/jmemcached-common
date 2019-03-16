package com.revenat.jmemcached.protocol;

import java.io.IOException;
import java.io.InputStream;

import com.revenat.jmemcached.protocol.model.Request;

/**
 * Responsible for reading {@link Request} object from {@link InputStream}
 * 
 * @author Vitaly Dragun
 *
 */
public interface RequestReader {

	/**
	 * Reads a {@link Request} object from the provided {@link InputStream}
	 * @throws IOException
	 */
	Request readFrom(InputStream input) throws IOException;
}
