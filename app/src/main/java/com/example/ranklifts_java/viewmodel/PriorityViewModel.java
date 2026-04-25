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
import com.example.ranklifts_java.api.RecalculationResponse;
import com.example.ranklifts_java.models.CategoryRank;
import com.example.ranklifts_java.models.CategoryRankRequest;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriorityViewModel extends AndroidViewModel {

    public PriorityViewModel(@NonNull Application application) {
        super(application);
        init(application);  // auto-init
    }
    private ApiService apiService;
    private SharedPreferences prefs;
    private Gson gson;

    // Priority data structure
    public static class PriorityData {
        private String movementType;
        private String firstPriority;
        private String secondPriority;
        private String thirdPriority;
        private String fourthPriority;

        public PriorityData(String movementType, String first, String second, String third, String fourth) {
            this.movementType = movementType;
            this.firstPriority = first;
            this.secondPriority = second;
            this.thirdPriority = third;
            this.fourthPriority = fourth;
        }

        public String getMovementType() { return movementType; }
        public String getFirstPriority() { return firstPriority; }
        public String getSecondPriority() { return secondPriority; }
        public String getThirdPriority() { return thirdPriority; }
        public String getFourthPriority() { return fourthPriority; }

        // Helper for MovementPriorityActivity
        public String[] getPriorityOrder() {
            return new String[]{firstPriority, secondPriority, thirdPriority, fourthPriority};
        }

        public boolean isComplete() {
            return firstPriority != null && !firstPriority.isEmpty() &&
                    secondPriority != null && !secondPriority.isEmpty() &&
                    thirdPriority != null && !thirdPriority.isEmpty() &&
                    fourthPriority != null && !fourthPriority.isEmpty();
        }

        public CategoryRankRequest toRequest() {
            // Normalize strings to match backend expectations (e.g. "Push", "Compound")
            return new CategoryRankRequest(
                    normalize(movementType),
                    normalize(firstPriority),
                    normalize(secondPriority),
                    normalize(thirdPriority),
                    normalize(fourthPriority)
            );
        }

        private String normalize(String input) {
            if (input == null || input.isEmpty()) return "";
            String trimmed = input.trim();
            // Only capitalize first letter, leave the rest untouched
            return trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1);
        }
    }

    // LiveData
    private MutableLiveData<PriorityData> pushPriority = new MutableLiveData<>();
    private MutableLiveData<PriorityData> pullPriority = new MutableLiveData<>();
    private MutableLiveData<PriorityData> legsPriority = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>(false);

    // Default priorities
    private static final String DEFAULT_COMPOUND = "Compound";
    private static final String DEFAULT_CALISTHENICS = "Calisthenics";
    private static final String DEFAULT_PRIMARY = "PrimaryIsolation";
    private static final String DEFAULT_PURE = "PureIsolation";

    public void init(Context context) {
        apiService = ApiClient.getApiService(context);
        prefs = context.getSharedPreferences("priority_data", Context.MODE_PRIVATE);
        gson = new Gson();

        loadSavedPriorities();
    }

    private void loadSavedPriorities() {
        String pushJson = prefs.getString("push_priority", "");
        String pullJson = prefs.getString("pull_priority", "");
        String legsJson = prefs.getString("legs_priority", "");

        if (!pushJson.isEmpty()) {
            pushPriority.setValue(gson.fromJson(pushJson, PriorityData.class));
        } else {
            pushPriority.setValue(new PriorityData("Push", DEFAULT_COMPOUND, DEFAULT_CALISTHENICS, DEFAULT_PRIMARY, DEFAULT_PURE));
        }

        if (!pullJson.isEmpty()) {
            pullPriority.setValue(gson.fromJson(pullJson, PriorityData.class));
        } else {
            pullPriority.setValue(new PriorityData("Pull", DEFAULT_COMPOUND, DEFAULT_CALISTHENICS, DEFAULT_PRIMARY, DEFAULT_PURE));
        }

        if (!legsJson.isEmpty()) {
            legsPriority.setValue(gson.fromJson(legsJson, PriorityData.class));
        } else {
            legsPriority.setValue(new PriorityData("Legs", DEFAULT_COMPOUND, DEFAULT_CALISTHENICS, DEFAULT_PRIMARY, DEFAULT_PURE));
        }
    }

    public void updatePriority(String movementType, String first, String second, String third, String fourth) {
        String capitalizedType = movementType.trim().substring(0, 1).toUpperCase() +
                movementType.trim().substring(1).toLowerCase();

        PriorityData data = new PriorityData(capitalizedType, first, second, third, fourth);

        switch (movementType.toLowerCase().trim()) {
            case "push":
                pushPriority.setValue(data);
                break;
            case "pull":
                pullPriority.setValue(data);
                break;
            case "legs":
                legsPriority.setValue(data);
                break;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(movementType.toLowerCase().trim() + "_priority", gson.toJson(data));
        editor.apply();
    }

    public void saveAllPriorities() {
        isLoading.setValue(true);
        saveSuccess.setValue(false);

        PriorityData push = pushPriority.getValue();
        PriorityData pull = pullPriority.getValue();
        PriorityData legs = legsPriority.getValue();

        if (push == null || pull == null || legs == null) {
            isLoading.setValue(false);
            errorMessage.setValue("Please configure all priorities first");
            return;
        }

        // Save sequentially instead of in parallel — eliminates the race condition
        apiService.calculateCategoryRank(push.toRequest()).enqueue(new Callback<CategoryRank>() {
            @Override
            public void onResponse(Call<CategoryRank> call, Response<CategoryRank> response) {
                if (!response.isSuccessful()) {
                    isLoading.setValue(false);
                    errorMessage.setValue("Failed to save push priorities");
                    return;
                }
                // Push saved — now save pull
                apiService.calculateCategoryRank(pull.toRequest()).enqueue(new Callback<CategoryRank>() {
                    @Override
                    public void onResponse(Call<CategoryRank> call, Response<CategoryRank> response) {
                        if (!response.isSuccessful()) {
                            isLoading.setValue(false);
                            errorMessage.setValue("Failed to save pull priorities");
                            return;
                        }
                        // Pull saved — now save legs
                        apiService.calculateCategoryRank(legs.toRequest()).enqueue(new Callback<CategoryRank>() {
                            @Override
                            public void onResponse(Call<CategoryRank> call, Response<CategoryRank> response) {
                                if (!response.isSuccessful()) {
                                    isLoading.setValue(false);
                                    errorMessage.setValue("Failed to save legs priorities");
                                    return;
                                }
                                // All 3 saved — now trigger recalculation
                                triggerRecalculation();
                            }

                            @Override
                            public void onFailure(Call<CategoryRank> call, Throwable t) {
                                isLoading.setValue(false);
                                errorMessage.setValue("Network error saving legs: " + t.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<CategoryRank> call, Throwable t) {
                        isLoading.setValue(false);
                        errorMessage.setValue("Network error saving pull: " + t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<CategoryRank> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error saving push: " + t.getMessage());
            }
        });
    }

    private void triggerRecalculation() {
        apiService.recalculateAll().enqueue(new Callback<RecalculationResponse>() {
            @Override
            public void onResponse(Call<RecalculationResponse> call, Response<RecalculationResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    saveSuccess.setValue(true);
                } else {
                    errorMessage.setValue("Priorities saved but ranking calculation failed");
                }
            }

            @Override
            public void onFailure(Call<RecalculationResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error during recalculation: " + t.getMessage());
            }
        });
    }

    private void checkCompletion(int completed, int errors) {
        if (completed == 3) {
            isLoading.setValue(false);
            if (errors == 0) {
                saveSuccess.setValue(true);
            } else {
                errorMessage.setValue("Failed to sync some priorities. Check connection.");
            }
        }
    }

    public void loadPriorities() {
        pushPriority.setValue(pushPriority.getValue());
        pullPriority.setValue(pullPriority.getValue());
        legsPriority.setValue(legsPriority.getValue());
    }

    // Helper for PrioritySetupActivity
    public boolean hasAllPriorities() {
        PriorityData push = pushPriority.getValue();
        PriorityData pull = pullPriority.getValue();
        PriorityData legs = legsPriority.getValue();

        return push != null && push.isComplete() &&
                pull != null && pull.isComplete() &&
                legs != null && legs.isComplete();
    }

    public LiveData<PriorityData> getPushPriority() { return pushPriority; }
    public LiveData<PriorityData> getPullPriority() { return pullPriority; }
    public LiveData<PriorityData> getLegsPriority() { return legsPriority; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Boolean> getSaveSuccess() { return saveSuccess; }
}