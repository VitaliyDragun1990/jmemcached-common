package com.revenat.jmemcached.protocol.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.revenat.jmemcached.protocol.ResponseReader;
import com.revenat.jmemcached.protocol.ResponseWriter;
import com.revenat.jmemcached.protocol.model.Response;
import com.revenat.jmemcached.protocol.model.Status;

/**
 * Component responsible for writing {@link Response} object into the {@link OutputStream}
 * and reading {@link Response} object from the {@link InputStream}
 * 
 * @author Vitaly Dragun
 *
 */
public class ResponseConverter extends AbstractPackageConverter implements ResponseReader, ResponseWriter {

	@Override
	public void writeTo(OutputStream output, Response response) throws IOException {
		DataOutputStream dataOutput = new DataOutputStream(output);
		
		dataOutput.writeByte(getVersionByte());
		dataOutput.writeByte(response.getStatus().getByteCode());
		dataOutput.writeByte(response.hasData() ? 1 : 0);
		writeDataIfAny(response, dataOutput);
		
		dataOutput.flush();
	}

	private void writeDataIfAny(Response response, DataOutputStream dataOutput) throws IOException {
		if (response.hasData()) {
			dataOutput.writeInt(response.getData().length);
			dataOutput.write(response.getData());
		}
	}

	@Override
	public Response readFrom(InputStream input) throws IOException {
		DataInputStream dataInput = new DataInputStream(input);
		
		byte versionByte = dataInput.readByte();
		checkProtocolVersion(versionByte);
		
		byte statusByte = dataInput.readByte();
		byte dataFlag = dataInput.readByte();
		if (dataFlag == 0) {
			return Response.empty(Status.valueOf(statusByte));
		} else {
			int dataLength = dataInput.readInt();
			byte[] data = IOUtils.readFully(dataInput, dataLength);
			
			return Response.withData(Status.valueOf(statusByte), data);
		}
	}
}
