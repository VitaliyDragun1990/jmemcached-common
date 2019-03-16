package com.revenat.jmemcached.protocol;

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
	 * @return deserialized object
	 */
	Object fromByteArray(byte[] data);
}
