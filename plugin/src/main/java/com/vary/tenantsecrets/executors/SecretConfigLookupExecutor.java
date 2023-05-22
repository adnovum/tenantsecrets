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

package com.vary.tenantsecrets.executors;

import com.github.bdpiparva.plugin.base.executors.secrets.LookupExecutor;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.vary.tenantsecrets.crypto.AesEncrypter;
import com.vary.tenantsecrets.crypto.ContextSpecificSecretProvider;
import com.vary.tenantsecrets.crypto.HKDFKeyDeriver;
import com.vary.tenantsecrets.crypto.KeyProvider;
import com.vary.tenantsecrets.models.LookupSecretRequest;
import com.vary.tenantsecrets.models.ResolvedSecret;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static com.github.bdpiparva.plugin.base.GsonTransformer.toJson;

public class SecretConfigLookupExecutor extends LookupExecutor<LookupSecretRequest> {

    private static final String DEFAULT_CIPHER_FILE = "/godata/config/cipher.aes";

    private static final Logger LOG = Logger.getLoggerFor(SecretConfigLookupExecutor.class);

    private ContextSpecificSecretProvider secretProviderInstance;

    @Override
    protected GoPluginApiResponse execute(LookupSecretRequest request) {
        String tenantId = request.getConfig().getTenantId();
        String cipherFile = StringUtils.defaultIfEmpty(request.getConfig().getCipherFile(), DEFAULT_CIPHER_FILE);

        ContextSpecificSecretProvider secretProvider;
        try {
            secretProvider = getSecretProvider(cipherFile);
        } catch (IOException e) {
            LOG.error("Could not load secret provider for cipher file " + cipherFile,
                    e);
            String message = "Could not decrypt secret. Failed to load the secret provider. Check logs.";
            return new DefaultGoPluginApiResponse(404,
                    "{\"message\": \"" + message + "\"}");
        }

        List<ResolvedSecret> resolvedSecrets = new LinkedList<>();
        for (String secret : request.getKeys()) {
            try {
                String decrypted = secretProvider.decrypt(secret, tenantId);
                resolvedSecrets.add(new ResolvedSecret(secret, decrypted));
            } catch (Exception e) {
                LOG.error("Could not decrypt secret " + secret, e);
                String message = "Could not decrypt secret. Was it generated for the " +
                        "tenant identifier defined for this secret config (" + tenantId + ")?";
                return new DefaultGoPluginApiResponse(404,
                        "{\"message\": \"" + message + "\"}");
            }
        }

        return DefaultGoPluginApiResponse.success(toJson(resolvedSecrets));
    }

    @Override
    protected LookupSecretRequest parseRequest(String body) {
        return LookupSecretRequest.fromJSON(body);
    }

    private ContextSpecificSecretProvider getSecretProvider(String cipherFile) throws IOException {
        if (secretProviderInstance == null) {
            secretProviderInstance = new ContextSpecificSecretProvider(
                    new AesEncrypter(),
                    new HKDFKeyDeriver(),
                    KeyProvider.fromPath(Paths.get(cipherFile))
            );
        }
        return secretProviderInstance;
    }
}
