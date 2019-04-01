package com.revenat.jmemcached.protocol.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revenat.jmemcached.exception.JMemcachedException;

public class ObjectConverterTest {
	private static final Object UNSERIALIZABLE = new Object();

	private static final Thing SERIALIZABLE = new Thing("name", 10);

	@Rule
	public ExpectedException expected = ExpectedException.none();

	private ObjectConverter converter;

	@Before
	public void setUp() {
		converter = new ObjectConverter();
	}

	@Test
	public void shouldNotAllowToSerializeUnserializableObject() throws Exception {
		expected.expect(JMemcachedException.class);
		expected.expectMessage(containsString(String.format("Class %s should implement java.io.Serializable",
				UNSERIALIZABLE.getClass().getName())));

		converter.toByteArray(UNSERIALIZABLE);
	}

	@Test
	public void shouldNotAllowToSerializeNull() throws Exception {
		expected.expect(NullPointerException.class);
		expected.expectMessage(containsString("Object to serialize can not be null"));
		
		converter.toByteArray(null);
	}

	@Test
	public void shouldAllowToSerializeObject() throws Exception {
		byte[] data = converter.toByteArray(SERIALIZABLE);

		assertThat(data, equalTo(serializeToArray(SERIALIZABLE)));
	}

	@Test
	public void shouldThrowExceptionIfSerializationProcessFailed() throws Exception {
		expected.expect(JMemcachedException.class);
		expected.expectMessage(containsString("Can not serialize object into byte array"));
		expected.expectCause(isA(IOException.class));

		converter.toByteArray(new SerializableFailed());
	}

	@Test
	public void shouldReturnEmptyOptionalIfDeserializeEmptyData() throws Exception {
		Optional<Serializable> optional = converter.fromByteArray(new byte[0]);

		assertFalse("Optional should be empty", optional.isPresent());
	}

	@Test
	public void shouldReturnEmptyOptionalIfDeserializeNull() throws Exception {
		Optional<Serializable> optional = converter.fromByteArray(null);

		assertFalse("Optional should be empty", optional.isPresent());
	}

	@Test
	public void shouldAllowToGetOptionalWithDeserializedObject() throws Exception {
		byte[] data = serializeToArray(SERIALIZABLE);

		Optional<Serializable> optional = converter.fromByteArray(data);
		Object deserialized = optional.get();
		assertThat(deserialized, equalTo(SERIALIZABLE));
	}
	
	@Test
	public void shouldNotAllowToDeserializeInvalidData() throws Exception {
		byte[] invalidData = new byte[] {0, 0, 0};
		expected.expect(JMemcachedException.class);
		expected.expectMessage(containsString("Can not deserialize object from byte array"));
		
		converter.fromByteArray(invalidData);
	}

	private static byte[] serializeToArray(Thing object) throws IOException {
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
		objectOutput.writeObject(object);
		objectOutput.flush();
		return byteOutput.toByteArray();
	}

	private static class Thing implements Serializable {
		private static final long serialVersionUID = 1L;
		public String name;
		public int age;

		Thing(String name, int age) {
			this.name = name;
			this.age = age;
		}

		@Override
		public int hashCode() {
			return Objects.hash(age, name);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Thing other = (Thing) obj;
			return age == other.age && Objects.equals(name, other.name);
		}
	}
	
	private static class SerializableFailed implements Serializable {
		private void readObject(ObjectInputStream in) throws IOException {
			throw new IOException("Error during serialization");
		}
		
		private void writeObject(ObjectOutputStream out) throws IOException {
			throw new IOException("Error during deserialization");
		}
	}
}
