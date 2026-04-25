package com.example.ranklifts_java.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranklifts_java.R;
import com.example.ranklifts_java.adapters.MovementRankAdapter;
import com.example.ranklifts_java.models.CategoryRank;
import com.example.ranklifts_java.models.OverallRank;
import com.example.ranklifts_java.viewmodel.AuthViewModel;
import com.example.ranklifts_java.viewmodel.RankingsViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private TextView tvOverallRank, tvOverallScore, tvUsername;
    private RecyclerView rvMovements;
    private MaterialButton btnRefresh, btnLogout;

    private RankingsViewModel rankingsViewModel;
    private AuthViewModel authViewModel;
    private MovementRankAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rankingsViewModel = new ViewModelProvider(this).get(RankingsViewModel.class);
        rankingsViewModel.init(this);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.init(this);

        initViews();
        setupObservers();

        // Load rankings
        rankingsViewModel.loadAllRankings();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh rankings every time we return to home screen
        rankingsViewModel.loadAllRankings();
    }
    private void initViews() {
        tvOverallRank = findViewById(R.id.tv_overall_rank);
        tvOverallScore = findViewById(R.id.tv_overall_score);
        tvUsername = findViewById(R.id.tv_username);
        rvMovements = findViewById(R.id.rv_movements);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnLogout = findViewById(R.id.btn_logout);

        MaterialButton btnEditProfile = findViewById(R.id.btn_edit_profile);
        MaterialButton btnManageExercises = findViewById(R.id.btn_manage_exercises);


        // Setup username
        if (authViewModel.getCurrentUser() != null) {
            tvUsername.setText(authViewModel.getCurrentUser().getUsername().toUpperCase());
        }

        // Setup RecyclerView
        adapter = new MovementRankAdapter(new ArrayList<>());
        rvMovements.setLayoutManager(new LinearLayoutManager(this));
        rvMovements.setAdapter(adapter);

        btnRefresh.setOnClickListener(v -> rankingsViewModel.loadAllRankings());
        btnLogout.setOnClickListener(v -> logout());
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
        btnManageExercises.setOnClickListener(v -> showMovementSelectionDialog());
    }
    private void showMovementSelectionDialog() {
        String[] movements = {"Push", "Pull", "Legs"};

        new android.app.AlertDialog.Builder(this)
                .setTitle("SELECT MOVEMENT")
                .setItems(movements, (dialog, which) -> {
                    String movement = movements[which].toLowerCase();
                    Intent intent = new Intent(HomeActivity.this, ManageExercisesActivity.class);
                    intent.putExtra("movement_type", movement);
                    startActivity(intent);
                })
                .show();
    }
    private void setupObservers() {
        rankingsViewModel.getOverallRank().observe(this, overallRank -> {
            if (overallRank != null) {
                tvOverallRank.setText(overallRank.getRank());
                tvOverallScore.setText(String.format("%.1f", overallRank.getOverallScore()));
            }
        });

        rankingsViewModel.getMovementRanks().observe(this, movementRanks -> {
            if (movementRanks != null) {
                adapter.updateData(movementRanks);
            }
        });

        rankingsViewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });

        rankingsViewModel.getIsLoading().observe(this, isLoading -> {
            btnRefresh.setEnabled(!isLoading);
        });
    }

    private void logout() {
        authViewModel.logout();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}