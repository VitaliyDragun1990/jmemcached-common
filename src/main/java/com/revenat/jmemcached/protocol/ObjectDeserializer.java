package com.revenat.jmemcached.protocol;

import java.util.Optional;

/**
 * Responsible for deserializing any kind of object from byte array. This
 * interface represents technology-agnostic contract, which means that concrete
 * implementors can decide which technology they should use for deserialization.
 * 
 * @author Vitaly Dragun
 *
 */
public interface ObjectDeserializer {

	/**
	 * Deserializes object from array of bytes.
	 * 
	 * @param data array of bytes to deserialize object from
	 * @return {@link Optional} with result of the deserializing operation.
	 */
	Optional<Object> fromByteArray(byte[] data);
}
