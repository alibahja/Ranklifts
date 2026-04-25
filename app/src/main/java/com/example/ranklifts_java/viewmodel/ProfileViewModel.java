package com.example.ranklifts_java.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ranklifts_java.api.ApiClient;
import com.example.ranklifts_java.api.ApiService;
import com.example.ranklifts_java.models.Profile;
import com.example.ranklifts_java.models.ProfileRequest;
import com.example.ranklifts_java.repository.TokenRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {

    private ApiService apiService;
    private TokenRepository tokenRepository;

    private MutableLiveData<Profile> profile = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> profileCreated = new MutableLiveData<>(false);

    public void init(Context context) {
        apiService = ApiClient.getApiService(context);
        tokenRepository = TokenRepository.getInstance(context);
    }

    public LiveData<Profile> getProfile() {
        return profile;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getProfileCreated() {
        return profileCreated;
    }

    public void fetchProfile() {
        isLoading.setValue(true);

        apiService.getProfile().enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    profile.setValue(response.body());
                } else if (response.code() == 404) {
                    // Profile not found - that's fine
                    profile.setValue(null);
                } else {
                    errorMessage.setValue("Failed to fetch profile");
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void createProfile(int bodyweight) {
        isLoading.setValue(true);

        ProfileRequest request = new ProfileRequest(bodyweight);

        apiService.createProfile(request).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    profile.setValue(response.body());
                    tokenRepository.setHasCompletedProfile(true);
                    profileCreated.setValue(true);
                } else {
                    errorMessage.setValue("Failed to create profile");
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void updateProfile(int bodyweight) {
        isLoading.setValue(true);

        ProfileRequest request = new ProfileRequest(bodyweight);

        apiService.updateProfile(request).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    profile.setValue(response.body());
                    profileCreated.setValue(true);
                } else {
                    errorMessage.setValue("Failed to update profile");
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error: " + t.getMessage());
            }
        });
    }
}