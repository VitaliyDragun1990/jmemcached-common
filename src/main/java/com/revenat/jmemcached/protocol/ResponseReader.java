package com.revenat.jmemcached.protocol;

import java.io.IOException;
import java.io.InputStream;

import com.revenat.jmemcached.protocol.model.Response;

/**
 * Responsible for reading response from {@link InputStream}
 * 
 * @author Vitaly Dragun
 *
 */
public interface ResponseReader {

	/**
	 * Reads a {@link Response} object from the provided {@link InputStream}
	 * @throws IOException
	 */
	Response readFrom(InputStream input) throws IOException;
}
