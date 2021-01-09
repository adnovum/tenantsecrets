package com.vary.tenantsecrets.crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AesEncrypter implements Encrypter {

	private static final Base64.Encoder ENCODER = Base64.getEncoder();
	private static final Base64.Decoder DECODER = Base64.getDecoder();

	@Override
	public String encrypt(byte[] key, String plainText) throws CryptoException {
		try {
			byte[] initializationVector = createIV();
			Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			encryptCipher.init(Cipher.ENCRYPT_MODE,
					new SecretKeySpec(key, "AES"),
					new IvParameterSpec(initializationVector));

			byte[] bytesToEncrypt = plainText.getBytes(StandardCharsets.UTF_8);
			byte[] encryptedBytes = encryptCipher.doFinal(bytesToEncrypt);

			return String.join(":", "AES",
					ENCODER.encodeToString(initializationVector),
					ENCODER.encodeToString(encryptedBytes));
		}
		catch (Exception e) {
			throw new CryptoException("Could not encrypt", e);
		}
	}

	@Override
	public String decrypt(byte[] key, String cipherText) throws CryptoException {
		try {
			String[] splits = cipherText.split(":");

			String encodedIV = splits[1];
			String encodedCipherText = splits[2];

			byte[] initializationVector = DECODER.decode(encodedIV);
			Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			decryptCipher.init(Cipher.DECRYPT_MODE,
					new SecretKeySpec(key, "AES"),
					new IvParameterSpec(initializationVector));

			byte[] decryptedBytes = decryptCipher.doFinal(DECODER.decode(encodedCipherText));
			return new String(decryptedBytes, StandardCharsets.UTF_8);
		}
		catch (Exception e) {
			throw new CryptoException("Could not decrypt", e);
		}
	}

	private static byte[] createIV() throws NoSuchAlgorithmException {
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		keygen.init(128);
		return keygen.generateKey().getEncoded();
	}
}
