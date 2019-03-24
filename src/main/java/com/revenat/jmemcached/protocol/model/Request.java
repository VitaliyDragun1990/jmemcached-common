package com.revenat.jmemcached.protocol.model;

import static java.util.Objects.requireNonNull;

/**
 * This immutable component represents protocol's request package.
 * 
 * @author Vitaly Dragun
 *
 */
public class Request extends AbstractPackage {
	private static final String KEY_NOT_NULL_MESSAGE = "key can not be null";
	private static final String COMMAND_NOT_NULL_MESSAGE = "command can not be null";
	private static final String DATA_NOT_NULL_MESSAGE = "data can not be null";

	private final Command command;
	private final String key;
	private final Long ttl;

	/**
	 * Creates {@link Request} that contains only specified {@link Command}
	 * 
	 * @param command {@link Command} to create a {@link Request} with
	 * @throws NullPointerException if specified {@code command} is null
	 */
	public static Request empty(Command command) {
		requireNonNull(command, COMMAND_NOT_NULL_MESSAGE);
		return new Request(command, null, null, null);
	}

	/**
	 * Creates {@link Request} that contains only specified {@link Command} and
	 * {@code key}.
	 * 
	 * @param command {@link Command} to create a {@link Request} with
	 * @param key string identifier
	 * @throws NullPointerException if either {@code command} or {@code key} is null
	 */
	public static Request withKey(Command command, String key) {
		requireNonNull(command, COMMAND_NOT_NULL_MESSAGE);
		requireNonNull(key, KEY_NOT_NULL_MESSAGE);
		return new Request(command, key, null, null);
	}
	
	/**
	 * Creates {@link Request} that contains required {@link Command},
	 * {@code key}, {@code data} and optional {@code ttl} parameters.
	 * 
	 * @param command {@link Command} to create a {@link Request} with
	 * @param key string identifier
	 * @param data data to put into request
	 * @param ttl optional, represents time-to-live parameter.
	 * @throws NullPointerException if {@code command}, {@code key} or {@code data} is null
	 */
	public static Request withKeyAndData(Command command, String key, byte[] data, Long ttl) {
		requireNonNull(command, COMMAND_NOT_NULL_MESSAGE);
		requireNonNull(key, KEY_NOT_NULL_MESSAGE);
		requireNonNull(data, DATA_NOT_NULL_MESSAGE);
		return new Request(command, key, data, ttl);
	}

	Request(Command command, String key, byte[] data, Long ttl) {
		super(data);
		this.command = command;
		this.key = key;
		this.ttl = ttl;
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
			builder.append(" (").append(String.format("time-to-live=%d milliseconds", ttl)).append(')');
		}

		return builder.toString();
	}
}
