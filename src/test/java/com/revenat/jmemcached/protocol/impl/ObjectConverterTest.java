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
	public void throwsExceptionIfTryToSerializeObjectThatDoesNotImplementSerializable() throws Exception {
		expected.expect(JMemcachedException.class);
		expected.expectMessage(containsString(String.format("Class %s should implement java.io.Serializable",
				UNSERIALIZABLE.getClass().getName())));

		converter.toByteArray(UNSERIALIZABLE);
	}

	@Test
	public void returnsEmptyArrayIfSerialiseNull() throws Exception {
		assertThat(converter.toByteArray(null).length, is(0));
	}

	@Test
	public void returnsArrayWithSerializedObjectData() throws Exception {
		byte[] data = converter.toByteArray(SERIALIZABLE);

		assertThat(data, equalTo(serializeToArray(SERIALIZABLE)));
	}

	@Test
	public void throwsExceptionIfCanNotSerializeObject() throws Exception {
		expected.expect(JMemcachedException.class);
		expected.expectMessage(containsString("Can not serialize object into byte array"));
		expected.expectCause(isA(IOException.class));

		converter.toByteArray(new SerializableFailed());
	}

	@Test
	public void returnsEmptyOptionalIfTryToDeserializeFromEmptyArray() throws Exception {
		Optional<Object> optional = converter.fromByteArray(new byte[0]);

		assertFalse("Optional should be empty", optional.isPresent());
	}

	@Test
	public void returnsEmptyOptionalIfTryToDeserializeFromNullArray() throws Exception {
		Optional<Object> optional = converter.fromByteArray(null);

		assertFalse("Optional should be empty", optional.isPresent());
	}

	@Test
	public void returnsOptionalWithDeserializedObject() throws Exception {
		byte[] data = serializeToArray(SERIALIZABLE);

		Optional<Object> optional = converter.fromByteArray(data);
		assertTrue("Optional should contain an object", optional.isPresent());
	}

	@Test
	public void deserializesObjectFromByteArray() throws Exception {
		byte[] data = serializeToArray(SERIALIZABLE);

		Thing deserialized = (Thing) converter.fromByteArray(data).get();

		assertThat(deserialized, equalTo(SERIALIZABLE));
	}
	
	@Test
	public void throwsExceptionIfTryToDeserializeFromInvalidData() throws Exception {
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
