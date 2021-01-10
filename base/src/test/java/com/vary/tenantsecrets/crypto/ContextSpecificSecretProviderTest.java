package com.vary.tenantsecrets.crypto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContextSpecificSecretProviderTest {

	private static ContextSpecificSecretProvider secretProvider;

	@BeforeAll
	public static void setupClass() throws Exception {
		byte[] masterKey = KeyProvider.fromStream(ContextSpecificSecretProviderTest.class.getResourceAsStream(
				"/cipher.aes"));

		secretProvider = new ContextSpecificSecretProvider(
				new AesEncrypter(),
				new HKDFKeyDeriver(),
				masterKey
		);
	}

	@Test
	public void shouldEncrypt() throws Exception {
		String secret = secretProvider.encrypt("hello world", "my_group1");
		assertFalse(secret.isEmpty());
	}

	@Test
	public void shouldEncryptSamePlaintextDifferently() throws Exception {
		String secret = secretProvider.encrypt("hello world", "my_group1");
		String secret2 = secretProvider.encrypt("hello world", "my_group1");
		assertNotEquals(secret, secret2);
	}

	@Test
	public void shouldDecryptWithSameContext() throws Exception {
		String plain = "hello world";
		String secret = secretProvider.encrypt(plain, "my_group1");

		String decrypted = secretProvider.decrypt(secret, "my_group1");
		assertEquals(plain, decrypted);
	}

	@Test
	public void shouldNotDecryptWithDifferentContext() throws Exception {
		String plain = "hello world";
		String secret = secretProvider.encrypt(plain, "my_group1");

		assertThrows(CryptoException.class, () -> secretProvider.decrypt(secret, "my_other_group"));
	}
}
