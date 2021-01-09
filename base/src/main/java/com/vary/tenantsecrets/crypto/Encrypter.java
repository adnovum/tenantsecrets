package com.vary.tenantsecrets.crypto;

public interface Encrypter {

	String encrypt(byte[] key, String plainText) throws CryptoException;
	String decrypt(byte[] key, String cipherText) throws CryptoException;
}
