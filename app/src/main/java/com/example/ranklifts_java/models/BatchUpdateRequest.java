package com.example.ranklifts_java.models;

import com.google.gson.annotations.SerializedName;

public class BatchUpdateRequest {
    @SerializedName("performanceId")
    private Integer performanceId;  // null for new exercises

    @SerializedName("exerciseName")
    private String exerciseName;

    @SerializedName("addedWeight")
    private float addedWeight;

    @SerializedName("reps")
    private int reps;

    public BatchUpdateRequest(Integer performanceId, String exerciseName, float addedWeight, int reps) {
        this.performanceId = performanceId;
        this.exerciseName = exerciseName;
        this.addedWeight = addedWeight;
        this.reps = reps;
    }

    // Getters and Setters
    public Integer getPerformanceId() { return performanceId; }
    public void setPerformanceId(Integer performanceId) { this.performanceId = performanceId; }

    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }

    public float getAddedWeight() { return addedWeight; }
    public void setAddedWeight(float addedWeight) { this.addedWeight = addedWeight; }

    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }
}