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
import com.example.ranklifts_java.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ProgressBar progressBar;

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.init(this);

        // Check if already logged in
        if (authViewModel.isLoggedIn()) {
            navigateToNextScreen();
            return;
        }

        initViews();
        setupObservers();
    }

    private void initViews() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        progressBar = findViewById(R.id.progress_bar);

        btnLogin.setOnClickListener(v -> attemptLogin());
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void setupObservers() {
        authViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(android.view.View.VISIBLE);
                btnLogin.setEnabled(false);
            } else {
                progressBar.setVisibility(android.view.View.GONE);
                btnLogin.setEnabled(true);
            }
        });

        authViewModel.getAuthResponse().observe(this, authResponse -> {
            if (authResponse != null) {
                navigateToNextScreen();
            }
        });

        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password required");
            return;
        }

        authViewModel.login(email, password);
    }

    private void navigateToNextScreen() {
        boolean hasCompletedProfile = authViewModel.getCurrentUser() != null &&
                authViewModel.getCurrentUser().isHasCompletedProfile();

        if (hasCompletedProfile) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        } else {
            startActivity(new Intent(LoginActivity.this, ProfileSetupActivity.class));
        }
        finish();
    }
}
