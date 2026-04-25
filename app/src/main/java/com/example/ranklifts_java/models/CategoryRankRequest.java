package com.example.ranklifts_java.models;

import com.google.gson.annotations.SerializedName;

public class CategoryRankRequest {
    @SerializedName("movementType")
    private String movementType;

    @SerializedName("firstPriority")
    private String firstPriority;

    @SerializedName("secondPriority")
    private String secondPriority;

    @SerializedName("thirdPriority")
    private String thirdPriority;

    @SerializedName("fourthPriority")
    private String fourthPriority;

    public CategoryRankRequest(String movementType, String firstPriority, String secondPriority,
                               String thirdPriority, String fourthPriority) {
        this.movementType = movementType;
        this.firstPriority = firstPriority;
        this.secondPriority = secondPriority;
        this.thirdPriority = thirdPriority;
        this.fourthPriority = fourthPriority;
    }
    // In CategoryRankRequest.java
    @Override
    public String toString() {
        return "CategoryRankRequest{movementType='" + movementType +
                "', first='" + firstPriority + "', second='" + secondPriority + "'}";
    }
    // Getters and Setters
    public String getMovementType() { return movementType; }
    public String getFirstPriority() { return firstPriority; }
    public String getSecondPriority() { return secondPriority; }
    public String getThirdPriority() { return thirdPriority; }
    public String getFourthPriority() { return fourthPriority; }
}
