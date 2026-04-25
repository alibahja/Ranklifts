package com.example.ranklifts_java.models;

import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("profileId")
    private int profileId;

    @SerializedName("userId")
    private int userId;

    @SerializedName("bodyweight")
    private int bodyweight;

    // Constructors
    public Profile() {}

    public Profile(int userId, int bodyweight) {
        this.userId = userId;
        this.bodyweight = bodyweight;
    }

    // Getters
    public int getProfileId() { return profileId; }
    public int getUserId() { return userId; }
    public int getBodyweight() { return bodyweight; }

    // Setters
    public void setProfileId(int profileId) { this.profileId = profileId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setBodyweight(int bodyweight) { this.bodyweight = bodyweight; }
}