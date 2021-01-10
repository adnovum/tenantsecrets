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

package com.vary.tenantsecrets.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LookupSecretRequest {
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    @Expose
    @SerializedName("configuration")
    private SecretConfig config;

    @Expose
    @SerializedName("keys")
    private List<String> keys;

    public static LookupSecretRequest fromJSON(String requestBody) {
        return GSON.fromJson(requestBody, LookupSecretRequest.class);
    }

    public SecretConfig getConfig() {
        return config;
    }

    public List<String> getKeys() {
        return keys;
    }
}
