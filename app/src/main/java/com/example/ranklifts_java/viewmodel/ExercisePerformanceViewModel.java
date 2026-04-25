package com.example.ranklifts_java.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ranklifts_java.api.ApiClient;
import com.example.ranklifts_java.api.ApiService;
import com.example.ranklifts_java.models.BatchUpdateRequest;
import com.example.ranklifts_java.models.ExercisePerformance;
import com.example.ranklifts_java.models.ExercisePerformanceRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExercisePerformanceViewModel extends ViewModel {

    private ApiService apiService;

    private MutableLiveData<List<ExercisePerformance>> performances = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<ExercisePerformance> singlePerformance = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> operationSuccess = new MutableLiveData<>(false);

    public void init(Context context) {
        apiService = ApiClient.getApiService(context);
    }

    public LiveData<List<ExercisePerformance>> getPerformances() {
        return performances;
    }

    public LiveData<ExercisePerformance> getSinglePerformance() {
        return singlePerformance;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getOperationSuccess() {
        return operationSuccess;
    }

    public void calculateExerciseRank(String exerciseName, float addedWeight, int reps) {
        isLoading.setValue(true);
        operationSuccess.setValue(false);

        ExercisePerformanceRequest request = new ExercisePerformanceRequest(exerciseName, addedWeight, reps);

        apiService.calculateExerciseRank(request).enqueue(new Callback<ExercisePerformance>() {
            @Override
            public void onResponse(Call<ExercisePerformance> call, Response<ExercisePerformance> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    singlePerformance.setValue(response.body());
                    operationSuccess.setValue(true);
                } else {
                    errorMessage.setValue("Failed to calculate exercise rank");
                }
            }

            @Override
            public void onFailure(Call<ExercisePerformance> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void updateExerciseRank(int performanceId, String exerciseName, float addedWeight, int reps) {
        isLoading.setValue(true);
        operationSuccess.setValue(false);

        ExercisePerformanceRequest request = new ExercisePerformanceRequest(exerciseName, addedWeight, reps);

        apiService.updateExerciseRank(performanceId, request).enqueue(new Callback<ExercisePerformance>() {
            @Override
            public void onResponse(Call<ExercisePerformance> call, Response<ExercisePerformance> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    singlePerformance.setValue(response.body());
                    operationSuccess.setValue(true);
                } else {
                    errorMessage.setValue("Failed to update exercise rank");
                }
            }

            @Override
            public void onFailure(Call<ExercisePerformance> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void deleteExercise(int performanceId) {
        isLoading.setValue(true);
        operationSuccess.setValue(false);

        apiService.deleteExercise(performanceId).enqueue(new Callback<ExercisePerformance>() {
            @Override
            public void onResponse(Call<ExercisePerformance> call, Response<ExercisePerformance> response) {
                isLoading.setValue(false);

                if (response.isSuccessful()) {
                    operationSuccess.setValue(true);
                } else {
                    errorMessage.setValue("Failed to delete exercise");
                }
            }

            @Override
            public void onFailure(Call<ExercisePerformance> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void getPushPerformances() {
        isLoading.setValue(true);

        apiService.getPushPerformances().enqueue(new Callback<List<ExercisePerformance>>() {
            @Override
            public void onResponse(Call<List<ExercisePerformance>> call, Response<List<ExercisePerformance>> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    performances.setValue(response.body());
                } else {
                    performances.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<ExercisePerformance>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error: " + t.getMessage());
                performances.setValue(new ArrayList<>());
            }
        });
    }

    public void getPullPerformances() {
        isLoading.setValue(true);

        apiService.getPullPerformances().enqueue(new Callback<List<ExercisePerformance>>() {
            @Override
            public void onResponse(Call<List<ExercisePerformance>> call, Response<List<ExercisePerformance>> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    performances.setValue(response.body());
                } else {
                    performances.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<ExercisePerformance>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error: " + t.getMessage());
                performances.setValue(new ArrayList<>());
            }
        });
    }

    public void getLegsPerformances() {
        isLoading.setValue(true);

        apiService.getLegsPerformances().enqueue(new Callback<List<ExercisePerformance>>() {
            @Override
            public void onResponse(Call<List<ExercisePerformance>> call, Response<List<ExercisePerformance>> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    performances.setValue(response.body());
                } else {
                    performances.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<ExercisePerformance>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error: " + t.getMessage());
                performances.setValue(new ArrayList<>());
            }
        });
    }

    public void updateExerciseBatch(List<BatchUpdateRequest> requests) {
        isLoading.setValue(true);
        operationSuccess.setValue(false);

        apiService.updateExerciseBatch(requests).enqueue(new Callback<List<ExercisePerformance>>() {
            @Override
            public void onResponse(Call<List<ExercisePerformance>> call, Response<List<ExercisePerformance>> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    performances.setValue(response.body());
                    operationSuccess.setValue(true);
                } else {
                    errorMessage.setValue("Failed to update exercise batch");
                }
            }

            @Override
            public void onFailure(Call<List<ExercisePerformance>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error: " + t.getMessage());
            }
        });
    }
}