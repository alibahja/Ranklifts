package com.example.ranklifts_java.models;

import com.google.gson.annotations.SerializedName;

public class Exercise {

    @SerializedName("exerciseId")
    private int exerciseId;

    @SerializedName("name")
    private String name;

    @SerializedName("movementType")
    private String movementType;

    @SerializedName("exerciseType")
    private String exerciseType;

    @SerializedName("targetMuscleGroup")
    private String targetMuscleGroup;

    @SerializedName("movementfactor")
    private float movementFactor;

    @SerializedName("isolationfactor")
    private float isolationFactor;

    // Constructors
    public Exercise() {}

    // Getters
    public int getExerciseId() { return exerciseId; }
    public String getName() { return name; }
    public String getMovementType() { return movementType; }
    public String getExerciseType() { return exerciseType; }
    public String getTargetMuscleGroup() { return targetMuscleGroup; }
    public float getMovementFactor() { return movementFactor; }
    public float getIsolationFactor() { return isolationFactor; }

    // Setters
    public void setExerciseId(int exerciseId) { this.exerciseId = exerciseId; }
    public void setName(String name) { this.name = name; }
    public void setMovementType(String movementType) { this.movementType = movementType; }
    public void setExerciseType(String exerciseType) { this.exerciseType = exerciseType; }
    public void setTargetMuscleGroup(String targetMuscleGroup) { this.targetMuscleGroup = targetMuscleGroup; }
    public void setMovementFactor(float movementFactor) { this.movementFactor = movementFactor; }
    public void setIsolationFactor(float isolationFactor) { this.isolationFactor = isolationFactor; }
}