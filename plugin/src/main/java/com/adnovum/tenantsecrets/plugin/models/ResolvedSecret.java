package com.adnovum.tenantsecrets.plugin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResolvedSecret {

    @Expose
    @SerializedName("key")
    private String key;

    @Expose
    @SerializedName("value")
    private String value;

    public ResolvedSecret(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
