/*
 * Copyright 2019 ThoughtWorks, Inc.
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
/* Taken and adapted from github.com/gocd-contrib/secrets-skeleton-plugin. */

package com.vary.tenantsecrets.validators;

import com.github.bdpiparva.plugin.base.validation.ValidationResult;
import com.github.bdpiparva.plugin.base.validation.Validator;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.vary.tenantsecrets.crypto.KeyProvider;
import com.vary.tenantsecrets.models.SecretConfig;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class SecretConfigValidator implements Validator {

    private static final Logger LOG = Logger.getLoggerFor(SecretConfigValidator.class);

    static final String CIPHER_FILE_DOES_NOT_EXIST = "Cipher file does not exist";

    static final String GIVEN_PATH_IS_NOT_A_FILE = "Given path is not a file";

    @Override
    public ValidationResult validate(Map<String, String> requestBody) {
        ValidationResult validationResult = new ValidationResult();
        String cipherFile = requestBody.get(SecretConfig.CIPHER_FILE_PROPERTY);

        if (StringUtils.isNotEmpty(cipherFile)) {
            Path cipherFilePath = Paths.get(cipherFile);

            if (!Files.exists(cipherFilePath)) {
                validationResult.add(SecretConfig.CIPHER_FILE_PROPERTY, CIPHER_FILE_DOES_NOT_EXIST);
            } else if (!Files.isRegularFile(cipherFilePath)) {
                validationResult.add(SecretConfig.CIPHER_FILE_PROPERTY, GIVEN_PATH_IS_NOT_A_FILE);
            } else if (!Files.isReadable(cipherFilePath)) {
                validationResult.add(SecretConfig.CIPHER_FILE_PROPERTY, "Cipher file is not readable");
            } else {
                try {
                    KeyProvider.fromPath(cipherFilePath);
                } catch (IllegalArgumentException e) {
                    validationResult.add(SecretConfig.CIPHER_FILE_PROPERTY,
                            "File does not contain a valid cipher: " + e.getMessage());
                } catch (IOException e) {
                    LOG.error("Could not read cipher file " + cipherFile, e);
                    validationResult.add(SecretConfig.CIPHER_FILE_PROPERTY, "Error reading cipher file. Check logs.");
                }
            }
        }

        return validationResult;
    }
}
