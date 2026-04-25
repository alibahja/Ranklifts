package com.example.ranklifts_java.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ranklifts_java.api.ApiClient;
import com.example.ranklifts_java.api.ApiService;
import com.example.ranklifts_java.api.AuthInterceptor;
import com.example.ranklifts_java.models.AuthRequest;
import com.example.ranklifts_java.models.AuthResponse;
import com.example.ranklifts_java.repository.TokenRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends ViewModel {

    private ApiService apiService;
    private TokenRepository tokenRepository;

    private MutableLiveData<AuthResponse> authResponse = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public void init(Context context) {
        apiService = ApiClient.getApiService(context);
        tokenRepository = TokenRepository.getInstance(context);
    }

    public LiveData<AuthResponse> getAuthResponse() {
        return authResponse;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void register(String username, String email, String password) {
        isLoading.setValue(true);

        AuthRequest request = new AuthRequest(username,email, password);

        apiService.register(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse auth = response.body();
                    auth.setUsername(username); // Set username from registration

                    // Save token and user data
                    tokenRepository.saveAuthResponse(auth);
                    AuthInterceptor.setAuthToken(auth.getToken());

                    authResponse.setValue(auth);
                } else {
                    errorMessage.setValue("Registration failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void login(String email, String password) {
        isLoading.setValue(true);

        AuthRequest request = new AuthRequest(email, password);

        apiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse auth = response.body();

                    // Save token and user data
                    tokenRepository.saveAuthResponse(auth);
                    AuthInterceptor.setAuthToken(auth.getToken());

                    authResponse.setValue(auth);
                } else {
                    errorMessage.setValue("Invalid email or password");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Network error: " + t.getMessage());
            }
        });
    }

    public void logout() {
        tokenRepository.clear();
        AuthInterceptor.clearAuthToken();
        authResponse.setValue(null);
    }

    public boolean isLoggedIn() {
        return tokenRepository.isLoggedIn();
    }

    public AuthResponse getCurrentUser() {
        return tokenRepository.getUserData();
    }
}