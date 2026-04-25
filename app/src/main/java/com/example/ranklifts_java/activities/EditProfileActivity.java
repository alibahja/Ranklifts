package com.example.ranklifts_java.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import javax.security.auth.callback.Callback;
import com.example.ranklifts_java.R;
import com.example.ranklifts_java.api.ApiClient;
import com.example.ranklifts_java.api.ApiService;
import com.example.ranklifts_java.api.RecalculationResponse;
import com.example.ranklifts_java.viewmodel.AuthViewModel;
import com.example.ranklifts_java.viewmodel.ProfileViewModel;

import retrofit2.Call;
import retrofit2.Response;


public class EditProfileActivity extends AppCompatActivity {

    private EditText etBodyweight;
    private Button btnSave;
    private TextView tvUsername, tvEmail;
    private TextView tvCurrentWeight;

    private ProfileViewModel profileViewModel;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.init(this);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.init(this);

        initViews();
        setupObservers();

        // Load existing profile
        profileViewModel.fetchProfile();

        // Display user info
        if (authViewModel.getCurrentUser() != null) {
            tvUsername.setText(authViewModel.getCurrentUser().getUsername().toUpperCase());
            tvEmail.setText(authViewModel.getCurrentUser().getEmail());
        }
    }

    private void initViews() {
        etBodyweight = findViewById(R.id.et_bodyweight);
        btnSave = findViewById(R.id.btn_save);
        tvUsername = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        tvCurrentWeight = findViewById(R.id.tv_current_weight);

        btnSave.setOnClickListener(v -> updateProfile());
    }

    private void setupObservers() {
        profileViewModel.getProfile().observe(this, profile -> {
            if (profile != null) {
                etBodyweight.setText(String.valueOf(profile.getBodyweight()));
                tvCurrentWeight.setText("LAST RECORDED: " + profile.getBodyweight() + " KG");
                tvCurrentWeight.setVisibility(android.view.View.VISIBLE);
            }
        });

        profileViewModel.getProfileCreated().observe(this, success -> {
            if (success) {
                btnSave.setEnabled(false);
                btnSave.setText("RECALCULATING...");

                // Trigger full recalculation after bodyweight change
                ApiService apiService = ApiClient.getApiService(this);

                apiService.recalculateAllProfile().enqueue(new retrofit2.Callback<RecalculationResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<RecalculationResponse> call, retrofit2.Response<RecalculationResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            Toast.makeText(EditProfileActivity.this,
                                    "Profile updated and rankings recalculated!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditProfileActivity.this,
                                    "Profile updated. Rankings will refresh on home screen.", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }

                    @Override
                    public void onFailure(retrofit2.Call<RecalculationResponse> call, Throwable t) {
                        Toast.makeText(EditProfileActivity.this,
                                "Profile saved. Network error during recalculation.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

        profileViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });

        profileViewModel.getIsLoading().observe(this, isLoading -> {
            btnSave.setEnabled(!isLoading);
            btnSave.setText(isLoading ? "SYNCING..." : "RE-SYNC BIOMETRICS");
        });
    }

    private void updateProfile() {
        String weightStr = etBodyweight.getText().toString().trim();

        if (weightStr.isEmpty()) {
            etBodyweight.setError("Bodyweight required");
            return;
        }

        int bodyweight;
        try {
            bodyweight = Integer.parseInt(weightStr);
            if (bodyweight <= 0 || bodyweight > 300) {
                etBodyweight.setError("Bodyweight must be between 1 and 300 kg");
                return;
            }
        } catch (NumberFormatException e) {
            etBodyweight.setError("Invalid number");
            return;
        }

        // Check if updating existing or creating new
        if (profileViewModel.getProfile().getValue() != null) {
            profileViewModel.updateProfile(bodyweight);
        } else {
            profileViewModel.createProfile(bodyweight);
        }
    }
}
