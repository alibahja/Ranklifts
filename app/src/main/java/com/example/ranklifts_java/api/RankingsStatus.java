package com.example.ranklifts_java.api;

import com.google.gson.annotations.SerializedName;

public class RankingsStatus {
    @SerializedName("hasPushExercises")
    private boolean hasPushExercises;

    @SerializedName("hasPullExercises")
    private boolean hasPullExercises;

    @SerializedName("hasLegsExercises")
    private boolean hasLegsExercises;

    @SerializedName("pushExercisesCount")
    private int pushExercisesCount;

    @SerializedName("pullExercisesCount")
    private int pullExercisesCount;

    @SerializedName("legsExercisesCount")
    private int legsExercisesCount;

    @SerializedName("hasPushCategory")
    private boolean hasPushCategory;

    @SerializedName("hasPullCategory")
    private boolean hasPullCategory;

    @SerializedName("hasLegsCategory")
    private boolean hasLegsCategory;

    @SerializedName("hasOverallRank")
    private boolean hasOverallRank;

    @SerializedName("lastUpdated")
    private String lastUpdated;

    // Getters and Setters
    public boolean isHasPushExercises() { return hasPushExercises; }
    public void setHasPushExercises(boolean hasPushExercises) { this.hasPushExercises = hasPushExercises; }

    public boolean isHasPullExercises() { return hasPullExercises; }
    public void setHasPullExercises(boolean hasPullExercises) { this.hasPullExercises = hasPullExercises; }

    public boolean isHasLegsExercises() { return hasLegsExercises; }
    public void setHasLegsExercises(boolean hasLegsExercises) { this.hasLegsExercises = hasLegsExercises; }

    public int getPushExercisesCount() { return pushExercisesCount; }
    public void setPushExercisesCount(int pushExercisesCount) { this.pushExercisesCount = pushExercisesCount; }

    public int getPullExercisesCount() { return pullExercisesCount; }
    public void setPullExercisesCount(int pullExercisesCount) { this.pullExercisesCount = pullExercisesCount; }

    public int getLegsExercisesCount() { return legsExercisesCount; }
    public void setLegsExercisesCount(int legsExercisesCount) { this.legsExercisesCount = legsExercisesCount; }

    public boolean isHasPushCategory() { return hasPushCategory; }
    public void setHasPushCategory(boolean hasPushCategory) { this.hasPushCategory = hasPushCategory; }

    public boolean isHasPullCategory() { return hasPullCategory; }
    public void setHasPullCategory(boolean hasPullCategory) { this.hasPullCategory = hasPullCategory; }

    public boolean isHasLegsCategory() { return hasLegsCategory; }
    public void setHasLegsCategory(boolean hasLegsCategory) { this.hasLegsCategory = hasLegsCategory; }

    public boolean isHasOverallRank() { return hasOverallRank; }
    public void setHasOverallRank(boolean hasOverallRank) { this.hasOverallRank = hasOverallRank; }

    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }
}