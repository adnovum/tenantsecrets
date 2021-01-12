package com.vary.tenantsecrets.tenantsecrets;

import com.vary.tenantsecrets.crypto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class SecretsService {

	private final File cipherFile;
	private ContextSpecificSecretProvider secretProvider;

	public SecretsService(@Value("${tenantsecrets.cipherFile}") File cipherFile) {
		this.cipherFile = cipherFile;
	}

	public String encrypt(String plainText, String tenantId) throws IOException, CryptoException {
		return getSecretProvider().encrypt(plainText, tenantId);
	}

	private ContextSpecificSecretProvider getSecretProvider() throws IOException {
		if (secretProvider == null) {
			secretProvider = new ContextSpecificSecretProvider(
					new AesEncrypter(),
					new HKDFKeyDeriver(),
					KeyProvider.fromFile(cipherFile)
			);
		}
		return secretProvider;
	}
}
