package com.revenat.jmemcached.protocol.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.revenat.jmemcached.protocol.model.Response;
import com.revenat.jmemcached.protocol.model.Status;
import com.revenat.jmemcached.protocol.model.Version;

public class ResponseConverterTest {
	private static final byte SUPPORTED_VERSION_BYTECODE = Version.VERSION_1_0.getByteCode();
	private static final Response RESPONSE_WITH_DATA = Response.withData(Status.GOTTEN, new byte[] { 1, 2, 3 });
	private static final Response EMPTY_RESPONSE = Response.empty(Status.CLEARED);
	
	private ResponseConverter converter;

	@Before
	public void setUp() {
		converter = new ResponseConverter();
	}

	@Test
	public void writesResponseWitoutData() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		converter.writeTo(out, EMPTY_RESPONSE);

		assertWrittenCorrectly(EMPTY_RESPONSE, out);
	}

	@Test
	public void writesResponseWithData() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		converter.writeTo(out, RESPONSE_WITH_DATA);

		assertWrittenCorrectly(RESPONSE_WITH_DATA, out);
	}

	@Test
	public void readsResponseWithoutData() throws Exception {
		ByteArrayInputStream input = createInputStreamFor(EMPTY_RESPONSE);

		Response result = converter.readFrom(input);
		
		assertReadCorrectly(result, EMPTY_RESPONSE);
	}
	
	@Test
	public void readsResponseWithData() throws Exception {
		ByteArrayInputStream input = createInputStreamFor(RESPONSE_WITH_DATA);

		Response result = converter.readFrom(input);
		
		assertReadCorrectly(result, RESPONSE_WITH_DATA);
	}

	private static void assertReadCorrectly(Response result, Response source) {
		assertThat(result.getStatus(), equalTo(source.getStatus()));
		assertThat(result.getData(), equalTo(source.getData()));
		
	}

	private static ByteArrayInputStream createInputStreamFor(Response response) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		DataOutputStream dataOutput = new DataOutputStream(output);

		dataOutput.writeByte(SUPPORTED_VERSION_BYTECODE);
		dataOutput.writeByte(response.getStatus().getByteCode());
		dataOutput.writeByte(response.hasData() ? 1 : 0);
		if (response.hasData()) {
			dataOutput.writeInt(response.getData().length);
			dataOutput.write(response.getData());
		}
		dataOutput.flush();
		
		return new ByteArrayInputStream(output.toByteArray());
	}

	private static void assertWrittenCorrectly(Response response, ByteArrayOutputStream out) throws IOException {
		DataInputStream input = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));

		assertVersionAndStatus(response, input);

		assertData(response, input);
	}
	
	private static void assertVersionAndStatus(Response response, DataInputStream input) throws IOException {
		byte versionByte = input.readByte();
		byte statusByte = input.readByte();
		assertThat(versionByte, equalTo(SUPPORTED_VERSION_BYTECODE));
		assertThat(statusByte, equalTo(response.getStatus().getByteCode()));
	}

	private static void assertData(Response response, DataInputStream input) throws IOException {
		byte dataFlag = input.readByte();

		if (!response.hasData()) {
			assertNoData(dataFlag);
		} else {
			assertCorrectData(response, input, dataFlag);
		}
	}
	
	private static void assertNoData(byte dataFlag) {
		assertThat(dataFlag, is((byte) 0));
	}

	private static void assertCorrectData(Response response, DataInputStream input, byte dataFlag)
			throws IOException {
		assertThat(dataFlag, is((byte) 1));
		int dataLength = input.readInt();
		assertThat(dataLength, equalTo(response.getData().length));
		byte[] data = IOUtils.readFully(input, dataLength);
		assertThat(data, equalTo(response.getData()));
	}
}
