package com.example.mylap.responsive;

import com.google.gson.annotations.SerializedName;

public class LoginRes {
    @SerializedName("token")
    private String token;

    @SerializedName("status")
    private int status;

    @SerializedName("id")
    private String id;

    public int getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
