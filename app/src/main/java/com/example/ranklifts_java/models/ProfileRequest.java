package com.example.ranklifts_java.models;

import com.google.gson.annotations.SerializedName;

public class ProfileRequest {
    @SerializedName("bodyweight")
    private int bodyweight;

    public ProfileRequest(int bodyweight) {
        this.bodyweight = bodyweight;
    }

    public int getBodyweight() { return bodyweight; }
}