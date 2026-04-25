package com.example.ranklifts_java.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranklifts_java.R;
import com.example.ranklifts_java.adapters.ExerciseSummaryAdapter;
import com.example.ranklifts_java.viewmodel.ExerciseViewModel;

import java.util.List;

public class ExerciseSummaryActivity extends AppCompatActivity {

    private TextView tvTotalExercises;
    private RecyclerView rvSummary;
    private Button btnSubmit;

    private ExerciseViewModel viewModel;
    private ExerciseSummaryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_summary);

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(ExerciseViewModel.class);

        initViews();
        setupRecyclerView();
        setupObservers();

        loadSummaryData();
    }

    private void initViews() {
        tvTotalExercises = findViewById(R.id.tv_total_exercises);
        rvSummary = findViewById(R.id.rv_summary);
        btnSubmit = findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(v -> showConfirmDialog());
    }

    private void setupRecyclerView() {
        adapter = new ExerciseSummaryAdapter();
        rvSummary.setLayoutManager(new LinearLayoutManager(this));
        rvSummary.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            btnSubmit.setEnabled(!isLoading);
            if (isLoading) {
                btnSubmit.setText("SYNCING DATA...");
            } else {
                btnSubmit.setText("FINALIZE CALIBRATION");
            }
        });

        viewModel.getSaveSuccess().observe(this, success -> {
            if (success) {
                new AlertDialog.Builder(this)
                        .setTitle("SYNC COMPLETE")
                        .setMessage("Global rankings have been calibrated based on your performance metrics.")
                        .setPositiveButton("CONTINUE", (dialog, which) -> {
                            startActivity(new Intent(ExerciseSummaryActivity.this, PrioritySetupActivity.class));
                            finish();
                        })
                        .show();
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadSummaryData() {
        int total = 0;

        List<ExerciseViewModel.LocalExerciseEntry> push = viewModel.getExercises("push");
        List<ExerciseViewModel.LocalExerciseEntry> pull = viewModel.getExercises("pull");
        List<ExerciseViewModel.LocalExerciseEntry> legs = viewModel.getExercises("legs");

        Log.d("Summary", "Push exercises: " + push.size());
        Log.d("Summary", "Pull exercises: " + pull.size());
        Log.d("Summary", "Legs exercises: " + legs.size());

        total = push.size() + pull.size() + legs.size();

        tvTotalExercises.setText(total + " UNITS");

        adapter.updateData(push, pull, legs);
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("FINALIZE CALIBRATION")
                .setMessage("CALIBRATION WILL BE PERMANENT ONCE SYNCED. Continue?")
                .setPositiveButton("CONFIRM", (dialog, which) -> {
                    // DEBUG: Check exercises before submitting
                    int pushSize = viewModel.getExercises("push").size();
                    int pullSize = viewModel.getExercises("pull").size();
                    int legsSize = viewModel.getExercises("legs").size();
                    Log.d("Summary", "BEFORE SUBMIT - Push: " + pushSize + ", Pull: " + pullSize + ", Legs: " + legsSize);

                    viewModel.submitAllExercises();
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }
}