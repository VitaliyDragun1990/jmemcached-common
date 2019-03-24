package com.revenat.jmemcached.protocol.impl;

import static java.util.Objects.requireNonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Optional;

import com.revenat.jmemcached.exception.JMemcachedException;
import com.revenat.jmemcached.protocol.ObjectDeserializer;
import com.revenat.jmemcached.protocol.ObjectSerializer;

/**
 * Component responsible for serialization and deserialization of objects using
 * standard Java {@code serialization} mechanism.
 * 
 * @author Vitaly Dragun
 *
 */
public class ObjectConverter implements ObjectSerializer, ObjectDeserializer {

	/**
	 * Serializes object into byte array using standard Java {@code serialization}
	 * mechanism.
	 * 
	 * @param object object to serialize
	 * @return array with serialized object data or an empty array if specified
	 *         {@code object} is {@code null}
	 * @throws JMemcachedException if specified {@code object} is not implement
	 *                             {@link Serializable} interface.
	 */
	@Override
	public byte[] toByteArray(Object object) {
		requireNonNull(object, "Object to serialize can not be null");
		if (!(object instanceof Serializable)) {
			throw new JMemcachedException(
					"Class " + object.getClass().getName() + " should implement java.io.Serializable");
		}

		return serialize(object);
	}

	private byte[] serialize(Object object) {
		ByteArrayOutputStream byteOutput;
		try {
			byteOutput = new ByteArrayOutputStream();
			ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
			objectOutput.writeObject(object);
			objectOutput.flush();
		} catch (Exception e) {
			throw new JMemcachedException("Can not serialize object into byte array: " + e.getMessage(), e);
		}
		return byteOutput.toByteArray();
	}

	/**
	 * Deserialize object from byte array using standard Java
	 * {@code deserialization} mechanism.
	 * 
	 * @param data array of data to deserialize object from
	 * @return {@link Optional} that contains deserialized object or empty one if
	 *         the specified {@code data} array is null or empty.
	 * @throws JMemcachedException if some error occurs during deserialization.
	 */
	@Override
	public Optional<Object> fromByteArray(byte[] data) {
		if (data == null || data.length == 0) {
			return Optional.empty();
		}

		return deserialize(data);
	}

	private Optional<Object> deserialize(byte[] data) {
		try {
			ObjectInputStream objectInput = new ObjectInputStream(new ByteArrayInputStream(data));

			return Optional.of(objectInput.readObject());
		} catch (Exception e) {
			throw new JMemcachedException("Can not deserialize object from byte array: " + e.getMessage(), e);
		}
	}
}
