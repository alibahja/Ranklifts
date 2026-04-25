package com.example.ranklifts_java.api;

import com.example.ranklifts_java.models.CategoryRank;
import com.example.ranklifts_java.models.OverallRank;
import com.google.gson.annotations.SerializedName;

public class RecalculationResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("pushCategory")
    private CategoryRank pushCategory;

    @SerializedName("pullCategory")
    private CategoryRank pullCategory;

    @SerializedName("legsCategory")
    private CategoryRank legsCategory;

    @SerializedName("overallRank")
    private OverallRank overallRank;

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public CategoryRank getPushCategory() { return pushCategory; }
    public void setPushCategory(CategoryRank pushCategory) { this.pushCategory = pushCategory; }

    public CategoryRank getPullCategory() { return pullCategory; }
    public void setPullCategory(CategoryRank pullCategory) { this.pullCategory = pullCategory; }

    public CategoryRank getLegsCategory() { return legsCategory; }
    public void setLegsCategory(CategoryRank legsCategory) { this.legsCategory = legsCategory; }

    public OverallRank getOverallRank() { return overallRank; }
    public void setOverallRank(OverallRank overallRank) { this.overallRank = overallRank; }
}