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

package com.vary.tenantsecrets.models;

import com.github.bdpiparva.plugin.base.annotations.Property;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class SecretConfig {
    @Expose
    @Property(name = "cipherFile", required = true)
    @SerializedName("cipherFile")
    private String cipherFile;

    @Expose
    @Property(name = "pipelineGroup", required = true)
    @SerializedName("pipelineGroup")
    private String pipelineGroup;

    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    public static SecretConfig fromJSON(String requestBody) {
        return GSON.fromJson(requestBody, SecretConfig.class);
    }

    public String getCipherFile() {
        return cipherFile;
    }

    public String getPipelineGroup() {
        return pipelineGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecretConfig that = (SecretConfig) o;
        return Objects.equals(getCipherFile(), that.getCipherFile()) &&
                Objects.equals(getPipelineGroup(), that.getPipelineGroup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCipherFile(), getPipelineGroup());
    }
}
