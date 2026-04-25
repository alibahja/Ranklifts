package com.example.ranklifts_java.models;

import com.google.gson.annotations.SerializedName;

public class CategoryRank {

    @SerializedName("movementType")
    private String movementType;

    @SerializedName("categoryScore")
    private float categoryScore;

    @SerializedName("categoryRank")
    private String categoryRank;

    @SerializedName("calculatedAt")
    private String calculatedAt;

    // Constructors
    public CategoryRank() {}

    // Getters and Setters
    public String getMovementType() { return movementType; }
    public void setMovementType(String movementType) { this.movementType = movementType; }

    public float getCategoryScore() { return categoryScore; }
    public void setCategoryScore(float categoryScore) { this.categoryScore = categoryScore; }

    public String getCategoryRank() { return categoryRank; }
    public void setCategoryRank(String categoryRank) { this.categoryRank = categoryRank; }

    public String getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(String calculatedAt) { this.calculatedAt = calculatedAt; }
}