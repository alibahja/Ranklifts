package com.example.ranklifts_java.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ranklifts_java.api.ApiClient;
import com.example.ranklifts_java.api.ApiService;
import com.example.ranklifts_java.models.CategoryRank;
import com.example.ranklifts_java.models.ExercisePerformance;
import com.example.ranklifts_java.models.OverallRank;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingsViewModel extends ViewModel {

    private ApiService apiService;

    private MutableLiveData<OverallRank> overallRank = new MutableLiveData<>();
    private MutableLiveData<List<MovementRankItem>> movementRanks = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    // Track callbacks
    private int completedCount = 0;
    private List<MovementRankItem> results = new ArrayList<>();

    public static class MovementRankItem {
        private String movementType;
        private String categoryRank;
        private float categoryScore;
        private List<ExercisePerformance> exercises;

        public MovementRankItem(String movementType, String categoryRank, float categoryScore, List<ExercisePerformance> exercises) {
            this.movementType = movementType;
            this.categoryRank = categoryRank;
            this.categoryScore = categoryScore;
            this.exercises = exercises;
        }

        public String getMovementType() { return movementType; }
        public String getCategoryRank() { return categoryRank; }
        public float getCategoryScore() { return categoryScore; }
        public List<ExercisePerformance> getExercises() { return exercises; }

        public String getMovementTypeUpper() {
            return movementType.toUpperCase();
        }

        public int getRankColor() {
            String rank = categoryRank.toLowerCase();
            if (rank.contains("elite")) return 0xfff87171;      // Red
            if (rank.contains("diamond")) return 0xff60a5fa;   // Blue
            if (rank.contains("platinum")) return 0xff2dd4bf;  // Teal
            if (rank.contains("gold")) return 0xfffbbf24;      // Gold
            if (rank.contains("silver")) return 0xff94a3b8;    // Silver
            return 0xffd97706;  // Bronze
        }
    }

    public void init(Context context) {
        apiService = ApiClient.getApiService(context);
    }

    public LiveData<OverallRank> getOverallRank() {
        return overallRank;
    }

    public LiveData<List<MovementRankItem>> getMovementRanks() {
        return movementRanks;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadAllRankings() {
        isLoading.setValue(true);

        // Reset counters
        completedCount = 0;
        results.clear();

        // Load all three categories and overall rank in parallel
        Call<CategoryRank> pushCall = apiService.getCategoryRank("push");
        Call<CategoryRank> pullCall = apiService.getCategoryRank("pull");
        Call<CategoryRank> legsCall = apiService.getCategoryRank("legs");
        Call<OverallRank> overallCall = apiService.getOverallRank();

        // Execute all calls
        pushCall.enqueue(new CategoryCallback("push"));
        pullCall.enqueue(new CategoryCallback("pull"));
        legsCall.enqueue(new CategoryCallback("legs"));
        overallCall.enqueue(new OverallCallback());
    }

    private class CategoryCallback implements Callback<CategoryRank> {
        private String movementType;

        public CategoryCallback(String movementType) {
            this.movementType = movementType;
        }

        @Override
        public void onResponse(Call<CategoryRank> call, Response<CategoryRank> response) {
            if (response.isSuccessful() && response.body() != null) {
                CategoryRank rank = response.body();
                MovementRankItem item = new MovementRankItem(
                        movementType,
                        rank.getCategoryRank(),
                        rank.getCategoryScore(),
                        new ArrayList<>()
                );
                results.add(item);
            } else {
                // Add placeholder for missing data
                results.add(new MovementRankItem(movementType, "Not Ranked", 0f, new ArrayList<>()));
            }

            completedCount++;

            if (completedCount == 3) {
                movementRanks.setValue(new ArrayList<>(results));
            }
        }

        @Override
        public void onFailure(Call<CategoryRank> call, Throwable t) {
            results.add(new MovementRankItem(movementType, "Error", 0f, new ArrayList<>()));
            completedCount++;

            if (completedCount == 3) {
                movementRanks.setValue(new ArrayList<>(results));
            }
        }
    }

    private class OverallCallback implements Callback<OverallRank> {
        @Override
        public void onResponse(Call<OverallRank> call, Response<OverallRank> response) {
            isLoading.setValue(false);

            if (response.isSuccessful() && response.body() != null) {
                overallRank.setValue(response.body());
            } else {
                errorMessage.setValue("Failed to load overall rank");
            }
        }

        @Override
        public void onFailure(Call<OverallRank> call, Throwable t) {
            isLoading.setValue(false);
            errorMessage.setValue("Network error: " + t.getMessage());
        }
    }
}