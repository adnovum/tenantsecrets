package com.adnovum.tenantsecrets.base.crypto;

public class ContextSpecificSecretProvider {

	// TODO: cache derived keys?

	private final Encrypter encrypter;
	private final KeyDeriver keyDeriver;
	private final byte[] masterKey;

	public ContextSpecificSecretProvider(Encrypter encrypter, KeyDeriver keyDeriver,
										 byte[] masterKey) {
		this.encrypter = encrypter;
		this.keyDeriver = keyDeriver;
		this.masterKey = masterKey;
	}

	public String encrypt(String plainText, String context) throws CryptoException {
		byte[] contextKey = getContextKey(context);
		return encrypter.encrypt(contextKey, plainText);
	}

	public String decrypt(String secret, String context) throws CryptoException {
		byte[] contextKey = getContextKey(context);
		return encrypter.decrypt(contextKey, secret);
	}

	private byte[] getContextKey(String context) {
		return keyDeriver.deriveKey(masterKey, context);
	}
}
