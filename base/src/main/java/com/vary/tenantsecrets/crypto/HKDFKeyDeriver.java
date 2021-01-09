package com.vary.tenantsecrets.crypto;

import at.favre.lib.crypto.HKDF;
import com.google.common.io.BaseEncoding;

import java.nio.charset.StandardCharsets;

public class HKDFKeyDeriver implements KeyDeriver {

	private static final byte[] SALT = BaseEncoding.base16().decode(
			"B389085EC4EDB8CE9C8EA7E7B1AD25BE68C17E1764F83984AB49D70BD8511598");

	@Override
	public byte[] deriveKey(byte[] originalKey, String context) {
		HKDF hkdf = HKDF.fromHmacSha256();
		byte[] pseudoRandomKey = hkdf.extract(SALT, originalKey);
		return hkdf.expand(pseudoRandomKey, context.getBytes(StandardCharsets.UTF_8), 16);
	}
}
