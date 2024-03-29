/*
 * Copyright 2021 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* Taken and adapted from github.com/gocd/gocd. */

package com.adnovum.tenantsecrets.base.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AesEncrypter implements Encrypter {

	private static final String ALGORITHM = "AES/GCM/NoPadding";
	private static final Base64.Encoder ENCODER = Base64.getEncoder();
	private static final Base64.Decoder DECODER = Base64.getDecoder();

	@Override
	public String encrypt(byte[] key, String plainText) throws CryptoException {
		try {
			byte[] initializationVector = createIV();
			Cipher encryptCipher = Cipher.getInstance(ALGORITHM);

			encryptCipher.init(Cipher.ENCRYPT_MODE,
					new SecretKeySpec(key, "AES"),
					new GCMParameterSpec(initializationVector.length * 8, initializationVector));

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
			Cipher decryptCipher = Cipher.getInstance(ALGORITHM);
			decryptCipher.init(Cipher.DECRYPT_MODE,
					new SecretKeySpec(key, "AES"),
					new GCMParameterSpec(initializationVector.length * 8, initializationVector));

			byte[] decryptedBytes = decryptCipher.doFinal(DECODER.decode(encodedCipherText));
			return new String(decryptedBytes, StandardCharsets.UTF_8);
		}
		catch (Exception e) {
			throw new CryptoException("Could not decrypt", e);
		}
	}

	private static byte[] createIV() {
		byte[] iv = new byte[12];
		SecureRandom random = new SecureRandom();
		random.nextBytes(iv);
		return iv;
	}
}
