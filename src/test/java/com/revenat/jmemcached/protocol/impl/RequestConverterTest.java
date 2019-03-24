package com.revenat.jmemcached.protocol.impl;

import static com.revenat.jmemcached.protocol.impl.RequestConverter.DATA_FLAG;
import static com.revenat.jmemcached.protocol.impl.RequestConverter.KEY_FLAG;
import static com.revenat.jmemcached.protocol.impl.RequestConverter.MAX_KEY_LENGTH;
import static com.revenat.jmemcached.protocol.impl.RequestConverter.TTL_FLAG;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revenat.jmemcached.exception.JMemcachedException;
import com.revenat.jmemcached.protocol.model.Command;
import com.revenat.jmemcached.protocol.model.Request;
import com.revenat.jmemcached.protocol.model.Version;

public class RequestConverterTest {
	private static final long TTL = 1000;
	private static final byte[] DATA = new byte[] {1, 2, 3};
	private static final String KEY = "Key";
	private static final byte SUPPORTED_VERSION_BYTECODE = Version.VERSION_1_0.getByteCode();
	
	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	private RequestConverter converter;

	@Before
	public void setUp() {
		converter = new RequestConverter();
	}
	
	@Test
	public void shouldAllowToWriteEmptyRequest() throws Exception {
		Request request = Request.empty(Command.CLEAR);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		converter.writeTo(output, request);
		
		assertWrittenCorrectly(output, request);
	}
	
	@Test
	public void shouldAllowToWriteRequestWithKey() throws Exception {
		Request request = Request.withKey(Command.GET, KEY);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		converter.writeTo(output, request);
		
		assertWrittenCorrectly(output, request);
	}
	
	@Test
	public void shouldAllowToWriteRequestWithData() throws Exception {
		Request request = Request.withKeyAndData(Command.PUT, KEY, DATA, null);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		converter.writeTo(output, request);
		
		assertWrittenCorrectly(output, request);
	}
	
	@Test
	public void shouldAllowToWriteRequestWithTtl() throws Exception {
		Request request = Request.withKeyAndData(Command.PUT, KEY, DATA, TTL);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		converter.writeTo(output, request);
		
		assertWrittenCorrectly(output, request);
	}
	
	@Test
	public void shouldNotAllowToWriteRequestWhereKeyHasIllegalLength() throws Exception {
		String longKey = generateKeyWithLength(130);
		expected.expect(JMemcachedException.class);
		expected.expectMessage(containsString(
				String.format("Key length should be <= %d bytes for key = %s", MAX_KEY_LENGTH, longKey)));
		
		converter.writeTo(new ByteArrayOutputStream(), Request.withKey(Command.GET, longKey));
	}

	private String generateKeyWithLength(int length) {
		return StringUtils.repeat("a", length);
	}
	
	@Test
	public void shouldAllowToReadEmptyRequest() throws Exception {
		Request clearRequest = Request.empty(Command.CLEAR);
		assertReadsRequestCorrectly(clearRequest);
	}
	
	@Test
	public void shouldAllowToReadRequestWithKey() throws Exception {
		Request getRequest = Request.withKey(Command.GET, KEY);
		assertReadsRequestCorrectly(getRequest);
	}
	
	@Test
	public void shouldAllowToReadRequestWithData() throws Exception {
		Request putRequest = Request.withKeyAndData(Command.PUT, KEY, DATA, null);
		assertReadsRequestCorrectly(putRequest);
	}
	
	@Test
	public void shouldAllowToReadRequestWithTtl() throws Exception {
		Request putRequestWithTtl = Request.withKeyAndData(Command.PUT, KEY, DATA, TTL);
		assertReadsRequestCorrectly(putRequestWithTtl);
	}
	
	@Test
	public void shouldNotAllowToReadRequestWithUnsupportedVersion() throws Exception {
		ByteArrayInputStream input = createInputStreamWithUnsupportedVersionFor(Request.empty(Command.CLEAR));
		expected.expect(JMemcachedException.class);
		
		converter.readFrom(input);
	}
	
	private void assertReadsRequestCorrectly(Request request) throws IOException {
		ByteArrayInputStream input = createInputStreamFor(request);

		Request result = converter.readFrom(input);

		assertReadCorrectly(result, request);
	}
	
	private ByteArrayInputStream createInputStreamFor(Request request) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		converter.writeTo(output, request);
		return new ByteArrayInputStream(output.toByteArray());
	}
	
	private ByteArrayInputStream createInputStreamWithUnsupportedVersionFor(Request request) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		converter.writeTo(output, request);
		byte[] content = output.toByteArray();
		int versionPosition = 0;
		content[versionPosition] = 0b00000000;
		return new ByteArrayInputStream(content);
	}

	private static void assertReadCorrectly(Request result, Request source) {
		assertThat(result.getCommand(), equalTo(source.getCommand()));
		if (result.hasData()) {
			assertThat(result.getData(), equalTo(source.getData()));
		}
		if (result.hasTtl()) {
			assertThat(result.getTtl(), equalTo(source.getTtl()));
		}
		if (result.hasKey()) {
			assertThat(result.getKey(), equalTo(source.getKey()));
		}
		
	}

	private static void assertWrittenCorrectly(ByteArrayOutputStream output, Request request) throws IOException {
		DataInputStream input = new DataInputStream(new ByteArrayInputStream(output.toByteArray()));
		
		assertVersionAndCommand(request, input);
		
		assertFlags(request, input);
		
		assertKey(request, input);
		
		assertTtl(request, input);
		
		assertData(request, input);
	}

	private static void assertVersionAndCommand(Request request, DataInputStream input) throws IOException {
		byte versionByte = input.readByte();
		byte cmdByte = input.readByte();
		assertThat(versionByte, equalTo(SUPPORTED_VERSION_BYTECODE));
		assertThat(cmdByte, equalTo(request.getCommand().getByteCode()));
	}
	
	private static void assertFlags(Request request, DataInputStream input) throws IOException {
		byte flagsByte = input.readByte();
		if (isEmpty(request)) {
			assertEmptyFlags(flagsByte);
		}
		if (request.hasTtl()) {
			assertFlag(flagsByte, TTL_FLAG);
		}
		if (request.hasKey()) {
			assertFlag(flagsByte, KEY_FLAG);
		}
		if (request.hasData()) {
			assertFlag(flagsByte, DATA_FLAG);
		}
	}
	
	private static boolean isEmpty(Request request) {
		return !request.hasKey() && !request.hasTtl() && !request.hasData();
	}

	private static void assertEmptyFlags(byte flagsByte) {
		boolean hasKey = (byte) (flagsByte & KEY_FLAG) != 0;
		boolean hasTtl = (byte) (flagsByte & TTL_FLAG) != 0;
		boolean hasData = (byte) (flagsByte & DATA_FLAG) != 0;
		assertThat(hasKey, is(false));
		assertThat(hasTtl, is(false));
		assertThat(hasData, is(false));
	}
	
	private static void assertFlag(byte flagsByte, byte flag) {
		boolean hasFlag = (byte) (flagsByte & flag) != 0;
		assertThat(hasFlag, is(true));
	}

	private static void assertKey(Request request, DataInputStream input) throws IOException {
		if (request.hasKey()) {
			byte keyLength = input.readByte();
			assertThat(keyLength, equalTo((byte) request.getKey().getBytes(StandardCharsets.US_ASCII).length));
			String key = new String(IOUtils.readFully(input, keyLength), StandardCharsets.US_ASCII);
			assertThat(key, equalTo(request.getKey()));
		}
	}

	private static void assertTtl(Request request, DataInputStream input) throws IOException {
		if (request.hasTtl()) {
			long ttl = input.readLong();
			assertThat(ttl, equalTo(request.getTtl()));
		}
	}

	private static void assertData(Request request, DataInputStream input) throws IOException {
		if (request.hasData()) {
			int dataLength = input.readInt();
			assertThat(dataLength, equalTo(request.getData().length));
			byte[] data = IOUtils.readFully(input, dataLength);
			assertThat(data, equalTo(request.getData()));
		}
	}
}
