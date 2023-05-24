package com.adnovum.tenantsecrets.base.crypto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class HKDFKeyDeriverTest {

	private static byte[] key;
	private final HKDFKeyDeriver keyDeriver = new HKDFKeyDeriver();

	@BeforeAll
	static void setupClass() throws Exception {
		key = KeyProvider.fromStream(ContextSpecificSecretProviderTest.class.getResourceAsStream(
				"/cipher.aes"));
	}

	@Test
	void shouldDeriveValidAesKey() throws Exception {
		byte[] derivedKey = keyDeriver.deriveKey(key, "my_group1");
		assertEquals(16, derivedKey.length);
		String encrypted = new AesEncrypter().encrypt(derivedKey, "foobar");
		assertFalse(encrypted.isEmpty());
	}

	@Test
	void shouldGenerateSameKeyForSameContext() {
		byte[] derivedKey = keyDeriver.deriveKey(key, "my_group1");
		byte[] derivedKey2 = keyDeriver.deriveKey(key, "my_group1");
		assertArrayEquals(derivedKey, derivedKey2);
	}

	@Test
	void shouldGenerateDifferentKeysForDifferentContext() {
		byte[] derivedKey = keyDeriver.deriveKey(key, "my_group1");
		byte[] derivedKey2 = keyDeriver.deriveKey(key, "my_group2");
		assertFalse(Arrays.equals(derivedKey, derivedKey2));
	}
}
