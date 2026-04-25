package com.example.ranklifts_java.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.ranklifts_java.models.AuthResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class TokenRepository {
    private static final String PREFS_NAME = "ranklifts_secure_prefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USER_DATA = "user_data";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_HAS_PROFILE = "has_completed_profile";

    private static TokenRepository instance;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private TokenRepository(Context context) {
        gson = new Gson();
        try {
            // Create master key for encryption
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            // Initialize EncryptedSharedPreferences
            sharedPreferences = EncryptedSharedPreferences.create(
                    PREFS_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            Log.e("TokenRepository", "Failed to create encrypted preferences", e);
            // Fallback to regular SharedPreferences if encryption fails
            sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
    }

    public static synchronized TokenRepository getInstance(Context context) {
        if (instance == null) {
            instance = new TokenRepository(context.getApplicationContext());
        }
        return instance;
    }

    // Save complete auth response
    public void saveAuthResponse(AuthResponse authResponse) {
        if (authResponse == null) return;

        Log.d("TokenRepository", "Saving token: " + (authResponse.getToken() != null ? "YES (length: " + authResponse.getToken().length() + ")" : "NO"));
        Log.d("TokenRepository", "Username: " + authResponse.getUsername());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, authResponse.getToken());
        editor.putString(KEY_USER_DATA, gson.toJson(authResponse));
        editor.putInt(KEY_USER_ID, authResponse.getUserId());
        editor.putString(KEY_USERNAME, authResponse.getUsername());
        editor.putString(KEY_EMAIL, authResponse.getEmail());
        editor.putBoolean(KEY_HAS_PROFILE, authResponse.isHasCompletedProfile());
        editor.apply();

        Log.d("TokenRepository", "Token saved successfully");
    }

    // Get auth token
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    // Get full user data
    public AuthResponse getUserData() {
        String userDataJson = sharedPreferences.getString(KEY_USER_DATA, null);
        if (userDataJson != null) {
            return gson.fromJson(userDataJson, AuthResponse.class);
        }
        return null;
    }

    // Get user ID
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    // Get username
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    // Get email
    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    // Check if user has completed profile setup
    public boolean hasCompletedProfile() {
        return sharedPreferences.getBoolean(KEY_HAS_PROFILE, false);
    }

    // Update profile completion status
    public void setHasCompletedProfile(boolean completed) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_HAS_PROFILE, completed);

        // Also update the stored user data
        AuthResponse userData = getUserData();
        if (userData != null) {
            userData.setHasCompletedProfile(completed);
            editor.putString(KEY_USER_DATA, gson.toJson(userData));
        }
        editor.apply();
    }

    // Clear all data (logout)
    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        String token = getToken();
        return token != null && !token.isEmpty();
    }
}