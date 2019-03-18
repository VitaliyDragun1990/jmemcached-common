package com.revenat.jmemcached.protocol.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import com.revenat.jmemcached.exception.JMemcachedException;
import com.revenat.jmemcached.protocol.RequestReader;
import com.revenat.jmemcached.protocol.RequestWriter;
import com.revenat.jmemcached.protocol.model.Command;
import com.revenat.jmemcached.protocol.model.Request;

/**
 * Component responsible for writing {@link Request} object into the {@link OutputStream}
 * and reading {@link Request} object from the {@link InputStream}
 * 
 * @author Vitaly Dragun
 *
 */
public class RequestConverter extends AbstractPackageConverter implements RequestReader, RequestWriter {
	public static final int MAX_KEY_LENGTH = 127;
	/**
	 * This bit combination means request contains key length - 1 bite and key value  - var. number of bites
	 */
	static final byte KEY_FLAG = 0b00000001;
	/**
	 * This bit combination means request contains ttl - 8 bites.
	 */
	static final byte TTL_FLAG = 0b00000010;
	/**
	 * This bit combination means request contains data length -  4 bites, data value - var. number of bites
	 */
	static final byte DATA_FLAG = 0b00000100;

	@Override
	public void writeTo(OutputStream output, Request request) throws IOException {
		DataOutputStream dataOutput = new DataOutputStream(output);
		
		dataOutput.writeByte(getVersionByte());
		dataOutput.writeByte(request.getCommand().getByteCode());
		dataOutput.writeByte(generateFlagsFor(request));
		
		writeKeyIfPresent(request, dataOutput);
		writeTtlIfPresent(request, dataOutput);
		writeDataIfPresent(request, dataOutput);

		dataOutput.flush();
	}

	protected int generateFlagsFor(Request request) {
		byte requestFlags = 0b00000000;
		
		if (request.hasKey()) {
			requestFlags = (byte) (requestFlags | KEY_FLAG);
		}
		if (request.hasTtl()) {
			requestFlags = (byte) (requestFlags | TTL_FLAG);
		}
		if (request.hasData()) {
			requestFlags = (byte) (requestFlags | DATA_FLAG);
		}
		
		return requestFlags;
	}

	protected void writeKeyIfPresent(Request request, DataOutputStream dataOutput) throws IOException {
		if (request.hasKey()) {
			writeKey(dataOutput, request);
		}
	}
	
	protected void writeKey(DataOutputStream dataOutput, Request request) throws IOException {
		byte[] key = request.getKey().getBytes(StandardCharsets.US_ASCII);
		if (key.length > MAX_KEY_LENGTH) {
			throw new JMemcachedException("Key length should be <= "+ MAX_KEY_LENGTH +" bytes for key = " + request.getKey());
		}
		dataOutput.writeByte(key.length);
		dataOutput.write(key);
	}

	protected void writeTtlIfPresent(Request request, DataOutputStream dataOutput) throws IOException {
		if (request.hasTtl()) {
			dataOutput.writeLong(request.getTtl());
		}
	}

	protected void writeDataIfPresent(Request request, DataOutputStream dataOutput) throws IOException {
		if (request.hasData()) {
			dataOutput.writeInt(request.getData().length);
			dataOutput.write(request.getData());
		}
	}

	@Override
	public Request readFrom(InputStream input) throws IOException {
		DataInputStream dataInput = new DataInputStream(input);
		
		byte versionByte = dataInput.readByte();
		checkProtocolVersion(versionByte);
		
		byte cmdByte = dataInput.readByte();
		byte flagByte = dataInput.readByte();
		
		return buildRequest(cmdByte, flagByte, dataInput);
	}

	protected Request buildRequest(byte cmdByte, byte flagByte, DataInputStream dataInput) throws IOException {
		boolean hasKey = (flagByte & KEY_FLAG) != 0;
		boolean hasTtl = (flagByte & TTL_FLAG) != 0;
		boolean hasData = (flagByte & DATA_FLAG) != 0;
		
		if (hasKey && hasData) {
			return buildRequestWithData(cmdByte, hasTtl, dataInput);
		} else if (hasKey) {
			return buildRequestWithKey(cmdByte, dataInput);
		} else {
			return buildEmptyRequest(cmdByte);
		}
	}

	protected Request buildRequestWithData(byte cmdByte, boolean hasTtl, DataInputStream dataInput) throws IOException {
		byte keyLength = dataInput.readByte();
		byte[] keyValue = IOUtils.readFully(dataInput, keyLength);
		Long ttl = null;
		if (hasTtl) {
			ttl = dataInput.readLong();
		}
		int dataLength = dataInput.readInt();
		byte[] data = IOUtils.readFully(dataInput, dataLength);
		
		return new Request(Command.valueOf(cmdByte), new String(keyValue, StandardCharsets.US_ASCII), data, ttl);
	}

	protected Request buildRequestWithKey(byte cmdByte, DataInputStream dataInput) throws IOException {
		byte keyLength = dataInput.readByte();
		byte[] keyValue = IOUtils.readFully(dataInput, keyLength);
		
		return new Request(Command.valueOf(cmdByte), new String(keyValue, StandardCharsets.US_ASCII));
	}
	
	protected Request buildEmptyRequest(byte cmdByte) {
		return new Request(Command.valueOf(cmdByte));
	}
}
