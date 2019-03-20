package com.revenat.jmemcached.protocol.model;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * This immutable component represents protocol's response package.
 * 
 * @author Vitaly Dragun
 *
 */
public class Response extends AbstractPackage {
	private final Status status;

	/**
	 * Creates new {@link Response} object that contains only command {@link Status}
	 * without any data.
	 * 
	 * @param status command {@link Status} for this response
	 * @throws NullPointerException if provided {@code status} is {@code null}
	 */
	public static Response empty(Status status) {
		return new Response(status);
	}

	/**
	 * Creates new {@link Response} object that contains command {@link Status} and
	 * data of some kind.
	 * 
	 * @param status command {@link Status} of this response
	 * @param data   some kind of data of this response
	 * @throws NullPointerException if either provided {@code status} or
	 *                              {@code data} is {@code null}
	 */
	public static Response withData(Status status, byte[] data) {
		return new Response(status, data);
	}

	protected Response(Status status, byte[] data) {
		super(data);
		this.status = requireNonNull(status, "status can not be null");
	}

	protected Response(Status status) {
		this.status = requireNonNull(status, "status can not be null");
	}

	public Status getStatus() {
		return this.status;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Response other = (Response) obj;
		return status == other.status;
	}

	@Override
	public String toString() {
		String s = status.name();
		
		if (hasData()) {
			s += String.format(" [%d bytes]", getData().length);
		}
		
		return s;
	}
}
