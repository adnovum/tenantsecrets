package com.vary.tenantsecrets.tenantsecrets;

import com.vary.tenantsecrets.crypto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class SecretsService {

    private final Path cipherFile;
    private ContextSpecificSecretProvider secretProvider;

    public SecretsService(@Value("${tenantsecrets.cipherFile}") Path cipherFile) {
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
                    KeyProvider.fromPath(cipherFile)
            );
        }
        return secretProvider;
    }
}
