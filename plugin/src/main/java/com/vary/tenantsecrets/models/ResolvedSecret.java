package com.vary.tenantsecrets.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResolvedSecret {
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

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
