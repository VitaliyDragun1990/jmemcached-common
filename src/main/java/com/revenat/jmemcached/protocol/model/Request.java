package com.revenat.jmemcached.protocol.model;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * This immutable component represents protocol's request package.
 * 
 * @author Vitaly Dragun
 *
 */
public class Request extends AbstractPackage {
	private static final String KEY_NOT_NULL_MESSAGE = "key can not be null";

	private final Command command;
	private final String key;
	private final Long ttl;

	/**
	 * Creates CLEAR request
	 */
	public static Request clear() {
		return new Request(Command.CLEAR);
	}

	/**
	 * Creates GET request for specified {@code key}
	 * 
	 * @param key value represents key for the data we want to get
	 * @throws NullPointerException if provided {@code key} is {@code null}
	 */
	public static Request get(String key) {
		requireNonNull(key, KEY_NOT_NULL_MESSAGE);
		return new Request(Command.GET, key);
	}

	/**
	 * Creates REMOVE request for specified {@code key}
	 * 
	 * @param key value represents key for the data we want to remove
	 * @throws NullPointerException if provided {@code key} is {@code null}
	 */
	public static Request remove(String key) {
		requireNonNull(key, KEY_NOT_NULL_MESSAGE);
		return new Request(Command.REMOVE, key);
	}

	/**
	 * Creates PUT request for {@code data} with specified {@code key}
	 * 
	 * @param key  value represents key for the data we want to put
	 * @param data data we want to put
	 * @throws NullPointerException if either provided {@code key} or {@code data}
	 *                              is {@code null}
	 */
	public static Request put(String key, byte[] data) {
		requireNonNull(key, KEY_NOT_NULL_MESSAGE);
		return new Request(Command.PUT, key, data);
	}

	/**
	 * Creates PUT request for {@code data} with specified {@code key} with
	 * predefined ttl (time to live) parameter.
	 * 
	 * @param key  value represents key for the data we want to put
	 * @param data data we want to put
	 * @param ttl  represents duration in milliseconds after which data with specified key
	 *             should be automatically removed
	 * @throws NullPointerException if provided {@code key} is {@code null}
	 */
	public static Request put(String key, byte[] data, Long ttl) {
		requireNonNull(key, KEY_NOT_NULL_MESSAGE);
		return new Request(Command.PUT, key, data, ttl);
	}

	public Request(Command command, String key, byte[] data, Long ttl) {
		super(data);
		this.command = requireNonNull(command, "command can not be null");
		this.key = key;
		this.ttl = ttl;
	}

	public Request(Command command, String key, byte[] data) {
		this(command, key, data, null);
	}

	public Request(Command command, String key) {
		this(command, key, null);
	}

	public Request(Command command) {
		this(command, null);
	}

	public String getKey() {
		return key;
	}

	public Long getTtl() {
		return ttl;
	}

	public Command getCommand() {
		return command;
	}

	public boolean hasKey() {
		return key != null;
	}

	public boolean hasTtl() {
		return ttl != null;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(command, key, ttl);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Request other = (Request) obj;
		return command == other.command && Objects.equals(key, other.key) && Objects.equals(ttl, other.ttl);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getCommand().name());

		if (hasKey()) {
			builder.append('[').append(getKey()).append(']');
		}
		if (hasData()) {
			builder.append("=").append(getData().length).append(" bytes");
		}
		if (hasTtl()) {
			builder.append(" (").append(String.format("time-to-live=%d milliseconds", ttl))
			.append(')');
		}

		return builder.toString();
	}
}
