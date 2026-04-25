package com.example.ranklifts_java.models;

import com.google.gson.annotations.SerializedName;

public class OverallRank {

    @SerializedName("overallScore")
    private float overallScore;

    @SerializedName("rank")
    private String rank;

    @SerializedName("calculatedAt")
    private String calculatedAt;

    // Constructors
    public OverallRank() {}

    // Getters and Setters
    public float getOverallScore() { return overallScore; }
    public void setOverallScore(float overallScore) { this.overallScore = overallScore; }

    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }

    public String getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(String calculatedAt) { this.calculatedAt = calculatedAt; }
}