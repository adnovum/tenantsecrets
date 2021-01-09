package com.vary.tenantsecrets.crypto;

public interface KeyDeriver {

	byte[] deriveKey(byte[] originalKey, String context);
}
