package com.example.ranklifts_java.models;

import com.google.gson.annotations.SerializedName;

public class ExercisePerformanceRequest {
    @SerializedName("exerciseName")
    private String exerciseName;

    @SerializedName("AddedWeight")
    private float addedWeight;

    @SerializedName("Reps")
    private int reps;

    public ExercisePerformanceRequest(String exerciseName, float addedWeight, int reps) {
        this.exerciseName = exerciseName;
        this.addedWeight = addedWeight;
        this.reps = reps;
    }

    // Getters and Setters
    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }

    public float getAddedWeight() { return addedWeight; }
    public void setAddedWeight(float addedWeight) { this.addedWeight = addedWeight; }

    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }
}