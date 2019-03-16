package com.revenat.jmemcached.protocol;

/**
 * Responsible for serializing any kind of object into an array of bytes. This
 * interface represents technology-agnostic contract, which means that concrete
 * implementors can decide which technology they should use for serialization.
 * 
 * @author Vitaly Dragun
 *
 */
public interface ObjectSerializer {

	/**
	 * Serializes specified {@code object} into byte array.
	 * 
	 * @param object object to serialize
	 * @return byte array contained serialized object data.
	 */
	byte[] toByteArray(Object object);
}
