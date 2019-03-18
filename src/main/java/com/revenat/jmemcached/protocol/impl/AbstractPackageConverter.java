package com.revenat.jmemcached.protocol.impl;

import com.revenat.jmemcached.exception.JMemcachedConfigException;
import com.revenat.jmemcached.protocol.model.Version;

/**
 * Component with methods common for all package converters.
 * 
 * @author Vitaly Dragun
 *
 */
abstract class AbstractPackageConverter {
	private static final Version CURRENT_VERSION = Version.VERSION_1_0;

	/**
	 * Checks whether provided {@code versionByte} represents currently supported
	 * protocol {@link Version}
	 * 
	 * @param versionByte byte of the protocol version to check
	 * @throws JMemcachedConfigException if protocol version assumed by the provided
	 *                                   {@code versionByte} is not supported.
	 */
	protected void checkProtocolVersion(byte versionByte) {
		Version protocolVersion = Version.valueOf(versionByte);
		if (protocolVersion != CURRENT_VERSION) {
			throw new JMemcachedConfigException("Unsupported protocol version: " + protocolVersion);
		}
	}

	/**
	 * Returns version's byte of the currently supported protocol {@link Version}
	 */
	protected byte getVersionByte() {
		return CURRENT_VERSION.getByteCode();
	}
}
