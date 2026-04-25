package com.example.ranklifts_java.models;

import com.google.gson.annotations.SerializedName;

public class ExercisePerformance {

    @SerializedName("performanceId")
    private int performanceId;

    @SerializedName("userId")
    private int userId;

    @SerializedName("exerciseId")
    private int exerciseId;

    @SerializedName("exerciseName")
    private String exerciseName;

    @SerializedName("exerciseType")
    private String exerciseType;

    @SerializedName("addedWeight")
    private float addedWeight;

    @SerializedName("reps")
    private int reps;

    @SerializedName("estimated1RM")
    private float estimated1RM;

    @SerializedName("relativeStrength")
    private float relativeStrength;

    @SerializedName("exerciseRank")
    private String exerciseRank;

    @SerializedName("recordedAt")
    private String recordedAt;

    // Constructors
    public ExercisePerformance() {}

    // Getters and Setters
    public int getPerformanceId() { return performanceId; }
    public void setPerformanceId(int performanceId) { this.performanceId = performanceId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getExerciseId() { return exerciseId; }
    public void setExerciseId(int exerciseId) { this.exerciseId = exerciseId; }

    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }

    public String getExerciseType() { return exerciseType; }
    public void setExerciseType(String exerciseType) { this.exerciseType = exerciseType; }

    public float getAddedWeight() { return addedWeight; }
    public void setAddedWeight(float addedWeight) { this.addedWeight = addedWeight; }

    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }

    public float getEstimated1RM() { return estimated1RM; }
    public void setEstimated1RM(float estimated1RM) { this.estimated1RM = estimated1RM; }

    public float getRelativeStrength() { return relativeStrength; }
    public void setRelativeStrength(float relativeStrength) { this.relativeStrength = relativeStrength; }

    public String getExerciseRank() { return exerciseRank; }
    public void setExerciseRank(String exerciseRank) { this.exerciseRank = exerciseRank; }

    public String getRecordedAt() { return recordedAt; }
    public void setRecordedAt(String recordedAt) { this.recordedAt = recordedAt; }
}
