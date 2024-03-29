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

package com.adnovum.tenantsecrets.plugin.models;

import com.github.bdpiparva.plugin.base.annotations.Property;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class SecretConfig {

    public static final String TENANT_ID_PROPERTY = "tenantId";
    public static final String CIPHER_FILE_PROPERTY = "cipherFile";

    @Expose
    @Property(name = TENANT_ID_PROPERTY, required = true)
    @SerializedName(TENANT_ID_PROPERTY)
    private String tenantId;

    @Expose
    @Property(name = CIPHER_FILE_PROPERTY)
    @SerializedName(CIPHER_FILE_PROPERTY)
    private String cipherFile;

    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    public static SecretConfig fromJSON(String requestBody) {
        return GSON.fromJson(requestBody, SecretConfig.class);
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getCipherFile() {
        return cipherFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecretConfig that = (SecretConfig) o;
        return Objects.equals(getCipherFile(), that.getCipherFile()) &&
                Objects.equals(getTenantId(), that.getTenantId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCipherFile(), getTenantId());
    }
}
