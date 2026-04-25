package com.example.ranklifts_java.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ranklifts_java.api.ApiClient;
import com.example.ranklifts_java.api.ApiService;
import com.example.ranklifts_java.models.BatchUpdateRequest;
import com.example.ranklifts_java.models.ExercisePerformance;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseViewModel extends AndroidViewModel {

    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        // Auto-init here so you don't need to call init() manually
        init(application);
    }
    private ApiService apiService;
    private SharedPreferences prefs;
    private Gson gson;

    // Local exercise data (temporary storage before saving to server)
    private Map<String, List<LocalExerciseEntry>> localExercises = new HashMap<>();

    // LiveData
    private MutableLiveData<Integer> pushCount = new MutableLiveData<>(0);
    private MutableLiveData<Integer> pullCount = new MutableLiveData<>(0);
    private MutableLiveData<Integer> legsCount = new MutableLiveData<>(0);
    private MutableLiveData<List<ExercisePerformance>> pushExercises = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<ExercisePerformance>> pullExercises = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<ExercisePerformance>> legsExercises = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>(false);

    // Local exercise entry class
    public static class LocalExerciseEntry {
        private String id;
        private String exerciseId;
        private String exerciseName;
        private int reps;
        private float addedWeight;
        private boolean isBodyweight;
        private Integer performanceId; // For existing exercises

        public LocalExerciseEntry(String id, String exerciseId, String exerciseName, int reps, float addedWeight, boolean isBodyweight, Integer performanceId) {
            this.id = id;
            this.exerciseId = exerciseId;
            this.exerciseName = exerciseName;
            this.reps = reps;
            this.addedWeight = addedWeight;
            this.isBodyweight = isBodyweight;
            this.performanceId = performanceId;
        }

        // Getters and Setters
        public String getId() { return id; }
        public String getExerciseId() { return exerciseId; }
        public String getExerciseName() { return exerciseName; }
        public int getReps() { return reps; }
        public void setReps(int reps) { this.reps = reps; }
        public float getAddedWeight() { return addedWeight; }
        public void setAddedWeight(float addedWeight) { this.addedWeight = addedWeight; }
        public boolean isBodyweight() { return isBodyweight; }
        public Integer getPerformanceId() { return performanceId; }
        public void setPerformanceId(Integer performanceId) { this.performanceId = performanceId; }
    }

    public void init(Context context) {
        apiService = ApiClient.getApiService(context);
        prefs = context.getSharedPreferences("exercise_data", Context.MODE_PRIVATE);
        gson = new Gson();

        // Initialize local maps
        localExercises.put("push", new ArrayList<>());
        localExercises.put("pull", new ArrayList<>());
        localExercises.put("legs", new ArrayList<>());

        // Load saved local data
        loadLocalData();
    }

    private void loadLocalData() {
        String pushJson = prefs.getString("push_exercises", "");
        Log.d("ExerciseVM", "Push JSON: " + pushJson);
        String pullJson = prefs.getString("pull_exercises", "");
        Log.d("ExerciseVM", "Pull JSON: " + pullJson);
        String legsJson = prefs.getString("legs_exercises", "");
        Log.d("ExerciseVM", "Legs JSON: " + legsJson);

        Type type = new TypeToken<ArrayList<LocalExerciseEntry>>(){}.getType();

        if (!pushJson.isEmpty()) {
            List<LocalExerciseEntry> push = gson.fromJson(pushJson, type);
            localExercises.put("push", push);
            pushCount.setValue(push.size());
            Log.d("ExerciseVM", "Push count loaded: " + pushCount.getValue());
        }

        if (!pullJson.isEmpty()) {
            List<LocalExerciseEntry> pull = gson.fromJson(pullJson, type);
            localExercises.put("pull", pull);
            pullCount.setValue(pull.size());
            Log.d("ExerciseVM", "Pull count loaded: " + pullCount.getValue());
        }

        if (!legsJson.isEmpty()) {
            List<LocalExerciseEntry> legs = gson.fromJson(legsJson, type);
            localExercises.put("legs", legs);
            legsCount.setValue(legs.size());
            Log.d("ExerciseVM", "Leg count loaded: " + legsCount.getValue());
        }
    }

    private void saveLocalData() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("push_exercises", gson.toJson(localExercises.get("push")));
        editor.putString("pull_exercises", gson.toJson(localExercises.get("pull")));
        editor.putString("legs_exercises", gson.toJson(localExercises.get("legs")));
        editor.apply();
    }

    public void addExercise(String movementType, LocalExerciseEntry entry) {
        List<LocalExerciseEntry> exercises = localExercises.get(movementType);
        if (exercises != null) {
            exercises.add(entry);
            updateCount(movementType, exercises.size());
            saveLocalData();
        }
    }

    public void removeExercise(String movementType, String entryId) {
        List<LocalExerciseEntry> exercises = localExercises.get(movementType);
        if (exercises != null) {
            exercises.removeIf(e -> e.getId().equals(entryId));
            updateCount(movementType, exercises.size());
            saveLocalData();
        }
    }

    public void updateExercise(String movementType, String entryId, int reps, float addedWeight) {
        List<LocalExerciseEntry> exercises = localExercises.get(movementType);
        if (exercises != null) {
            for (LocalExerciseEntry entry : exercises) {
                if (entry.getId().equals(entryId)) {
                    entry.setReps(reps);
                    entry.setAddedWeight(addedWeight);
                    break;
                }
            }
            saveLocalData();
        }
    }

    public List<LocalExerciseEntry> getExercises(String movementType) {
        return localExercises.get(movementType) != null ? new ArrayList<>(localExercises.get(movementType)) : new ArrayList<>();
    }

    private void updateCount(String movementType, int count) {
        switch (movementType) {
            case "push": pushCount.setValue(count); break;
            case "pull": pullCount.setValue(count); break;
            case "legs": legsCount.setValue(count); break;
        }
    }

    public void loadExercisesFromServer() {
        isLoading.setValue(true);

        // Load all three movement types
        apiService.getPushPerformances().enqueue(new ExerciseCallback("push"));
        apiService.getPullPerformances().enqueue(new ExerciseCallback("pull"));
        apiService.getLegsPerformances().enqueue(new ExerciseCallback("legs"));
    }

    private class ExerciseCallback implements Callback<List<ExercisePerformance>> {
        private String movementType;

        ExerciseCallback(String movementType) {
            this.movementType = movementType;
        }

        @Override
        public void onResponse(Call<List<ExercisePerformance>> call, Response<List<ExercisePerformance>> response) {
            isLoading.setValue(false);

            if (response.isSuccessful() && response.body() != null) {
                switch (movementType) {
                    case "push": pushExercises.setValue(response.body()); break;
                    case "pull": pullExercises.setValue(response.body()); break;
                    case "legs": legsExercises.setValue(response.body()); break;
                }
            }
        }

        @Override
        public void onFailure(Call<List<ExercisePerformance>> call, Throwable t) {
            isLoading.setValue(false);
            errorMessage.setValue("Failed to load " + movementType + " exercises");
        }
    }

    public void submitAllExercises() {
        Log.d("ExerciseVM", "=== submitAllExercises CALLED ===");
        saveSuccess.setValue(false);
        // DEBUG: Log current local exercises
        for (String movement : new String[]{"push", "pull", "legs"}) {
            Log.d("ExerciseVM", movement + " exercises count: " + localExercises.get(movement).size());
        }
        isLoading.setValue(true);

        List<BatchUpdateRequest> allRequests = new ArrayList<>();

        for (String movementType : new String[]{"push", "pull", "legs"}) {
            for (LocalExerciseEntry entry : localExercises.get(movementType)) {
                allRequests.add(new BatchUpdateRequest(
                        entry.getPerformanceId(),
                        entry.getExerciseName(),
                        entry.getAddedWeight(),
                        entry.getReps()
                ));
            }
        }

        // DEBUG: Log what we're sending
        Log.d("ExerciseVM", "About to call API. Requests count: " + allRequests.size());
        Log.d("ExerciseVM", "API URL will be: batch-update");
        for (BatchUpdateRequest req : allRequests) {
            Log.d("ExerciseVM", "  → " + req.getExerciseName() +
                    " | weight=" + req.getAddedWeight() +
                    " | reps=" + req.getReps() +
                    " | perfId=" + req.getPerformanceId());
        }

        if (allRequests.isEmpty()) {
            isLoading.setValue(false);
            errorMessage.setValue("No exercises to submit");
            return;
        }

        apiService.updateExerciseBatch(allRequests).enqueue(new Callback<List<ExercisePerformance>>() {
            @Override
            public void onResponse(Call<List<ExercisePerformance>> call, Response<List<ExercisePerformance>> response) {
                isLoading.setValue(false);

                Log.d("ExerciseVM", "Batch update response code: " + response.code());

                if (response.isSuccessful()) {
                    saveSuccess.setValue(true);
                    clearLocalData();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e("ExerciseVM", "Batch update failed: " + errorBody);
                        errorMessage.setValue("Failed to submit exercises: " + response.code());
                    } catch (Exception e) {
                        errorMessage.setValue("Failed to submit exercises");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ExercisePerformance>> call, Throwable t) {
                isLoading.setValue(false);
                Log.e("ExerciseVM", "Network error: " + t.getMessage());
                errorMessage.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void clearLocalData() {
        localExercises.put("push", new ArrayList<>());
        localExercises.put("pull", new ArrayList<>());
        localExercises.put("legs", new ArrayList<>());
        pushCount.setValue(0);
        pullCount.setValue(0);
        legsCount.setValue(0);
        saveLocalData();
    }

    public boolean hasAllExercises() {
        return pushCount.getValue() != null && pushCount.getValue() > 0 &&
                pullCount.getValue() != null && pullCount.getValue() > 0 &&
                legsCount.getValue() != null && legsCount.getValue() > 0;
    }

    public void loadExerciseCounts() {
        // Just triggers observers with current values
        loadLocalData();
    }

    // Getters for LiveData
    public LiveData<Integer> getPushCount() { return pushCount; }
    public LiveData<Integer> getPullCount() { return pullCount; }
    public LiveData<Integer> getLegsCount() { return legsCount; }
    public LiveData<List<ExercisePerformance>> getPushExercises() { return pushExercises; }
    public LiveData<List<ExercisePerformance>> getPullExercises() { return pullExercises; }
    public LiveData<List<ExercisePerformance>> getLegsExercises() { return legsExercises; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Boolean> getSaveSuccess() { return saveSuccess; }
}
