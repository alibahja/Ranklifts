package com.example.ranklifts_java.models;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("userId")
    private int userId;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("token")
    private String token;

    @SerializedName("hasCompletedProfile")
    private boolean hasCompletedProfile;

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public boolean isHasCompletedProfile() { return hasCompletedProfile; }
    public void setHasCompletedProfile(boolean hasCompletedProfile) { this.hasCompletedProfile = hasCompletedProfile; }
}
