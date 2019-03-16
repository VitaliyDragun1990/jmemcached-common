package com.revenat.jmemcached.protocol.model;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * This immutable component represents protocol's request.
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
		return new Request(Command.GET, key);
	}

	/**
	 * Creates REMOVE request for specified {@code key}
	 * 
	 * @param key value represents key for the data we want to remove
	 * @throws NullPointerException if provided {@code key} is {@code null}
	 */
	public static Request remove(String key) {
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
		return new Request(Command.PUT, key, data);
	}

	/**
	 * Creates PUT request for {@code data} with specified {@code key} with
	 * predefined ttl (time to live) parameter.
	 * 
	 * @param key  value represents key for the data we want to put
	 * @param data data we want to put
	 * @param ttl  represents date in milliseconds when data with specified key
	 *             should be automatically removed
	 * @throws NullPointerException if either provided {@code key} or {@code data}
	 *                              is {@code null}
	 */
	public static Request put(String key, byte[] data, long ttl) {
		return new Request(Command.PUT, key, data, ttl);
	}

	protected Request(Command command, String key, byte[] data, Long ttl) {
		super(data);
		this.key = requireNonNull(key, KEY_NOT_NULL_MESSAGE);
		this.command = command;
		this.ttl = ttl;
	}

	protected Request(Command command, String key, byte[] data) {
		super(data);
		this.key = requireNonNull(key, KEY_NOT_NULL_MESSAGE);
		this.command = command;
		this.ttl = null;
	}

	protected Request(Command command, String key) {
		this.key = requireNonNull(key, KEY_NOT_NULL_MESSAGE);
		this.command = command;
		this.ttl = null;
	}

	protected Request(Command command) {
		this.command = command;
		this.key = null;
		this.ttl = null;
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
	public String toString() {
		StringBuilder builder = new StringBuilder(getCommand().name());

		if (hasKey()) {
			builder.append('[').append(getKey()).append(']');
		}
		if (hasData()) {
			builder.append("=").append(getData().length).append(" bytes");
		}
		if (hasTtl()) {
			builder.append(" (").append(LocalDateTime.ofInstant(Instant.ofEpochMilli(ttl), ZoneId.systemDefault())).append(')');
		}

		return builder.toString();
	}
}
