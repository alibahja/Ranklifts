package com.example.ranklifts_java.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ranklifts_java.R;
import com.example.ranklifts_java.api.AuthInterceptor;
import com.example.ranklifts_java.models.AuthResponse;
import com.example.ranklifts_java.viewmodel.AuthViewModel;
import com.example.ranklifts_java.viewmodel.ProfileViewModel;

public class ProfileSetupActivity extends AppCompatActivity {

    private EditText etBodyweight;
    private Button btnCreateProfile;
    private ProgressBar progressBar;
    private TextView tvUsername;

    private ProfileViewModel profileViewModel;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.init(this);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.init(this);

        // Detailed token check
        AuthResponse currentUser = authViewModel.getCurrentUser();
        if (currentUser != null) {
            String token = currentUser.getToken();
            boolean hasToken = token != null && !token.isEmpty();
            Toast.makeText(this, "User: " + currentUser.getUsername() + "\nToken: " + (hasToken ? "EXISTS (length: " + token.length() + ")" : "NULL"), Toast.LENGTH_LONG).show();

            // Manually set token in interceptor to be sure
            if (hasToken) {
                AuthInterceptor.setAuthToken(token);
                Toast.makeText(this, "Token manually set in interceptor", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "CurrentUser is NULL!", Toast.LENGTH_LONG).show();
        }

        initViews();
        setupObservers();

        // Display username
        if (authViewModel.getCurrentUser() != null) {
            tvUsername.setText(authViewModel.getCurrentUser().getUsername().toUpperCase());
        }
    }

    private void initViews() {
        etBodyweight = findViewById(R.id.et_bodyweight);
        btnCreateProfile = findViewById(R.id.btn_create_profile);
        progressBar = findViewById(R.id.progress_bar);
        tvUsername = findViewById(R.id.tv_username);

        btnCreateProfile.setOnClickListener(v -> createProfile());
    }

    private void setupObservers() {
        profileViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? android.view.View.VISIBLE : android.view.View.GONE);
            btnCreateProfile.setEnabled(!isLoading);
        });

        profileViewModel.getProfileCreated().observe(this, created -> {
            if (created) {
                // Update auth context
                if (authViewModel.getCurrentUser() != null) {
                    authViewModel.getCurrentUser().setHasCompletedProfile(true);
                }
                Intent intent = new Intent(ProfileSetupActivity.this, ExerciseSetupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        profileViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createProfile() {
        String bodyweightStr = etBodyweight.getText().toString().trim();

        if (TextUtils.isEmpty(bodyweightStr)) {
            etBodyweight.setError("Bodyweight required");
            return;
        }

        int bodyweight;
        try {
            bodyweight = Integer.parseInt(bodyweightStr);
            if (bodyweight <= 0 || bodyweight > 300) {
                etBodyweight.setError("Bodyweight must be between 1 and 300 kg");
                return;
            }
        } catch (NumberFormatException e) {
            etBodyweight.setError("Invalid number");
            return;
        }

        profileViewModel.createProfile(bodyweight);
    }
}
