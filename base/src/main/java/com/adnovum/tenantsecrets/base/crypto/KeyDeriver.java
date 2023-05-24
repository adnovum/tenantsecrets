package com.adnovum.tenantsecrets.base.crypto;

public interface KeyDeriver {

	byte[] deriveKey(byte[] originalKey, String context);
}
