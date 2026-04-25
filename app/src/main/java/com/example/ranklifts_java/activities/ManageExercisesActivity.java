package com.example.ranklifts_java.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import javax.security.auth.callback.Callback;

import com.example.ranklifts_java.R;
import com.example.ranklifts_java.adapters.ManageExercisesAdapter;
import com.example.ranklifts_java.api.ApiClient;
import com.example.ranklifts_java.api.ApiService;
import com.example.ranklifts_java.api.RecalculationResponse;
import com.example.ranklifts_java.models.ExercisePerformance;
import com.example.ranklifts_java.repository.ExerciseRepository;
import com.example.ranklifts_java.viewmodel.ExercisePerformanceViewModel;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class ManageExercisesActivity extends AppCompatActivity {

    private String movementType;
    private TextView tvTitle, tvCount;
    private RecyclerView rvExercises;
    private Button btnAdd, btnSave;

    private ExercisePerformanceViewModel viewModel;
    private ManageExercisesAdapter adapter;
    private List<ExercisePerformance> exercises = new ArrayList<>();
    private List<ExercisePerformance> deletedExercises = new ArrayList<>();
    private List<ExercisePerformance> modifiedExercises = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_exercises);

        movementType = getIntent().getStringExtra("movement_type");
        if (movementType == null) {
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(ExercisePerformanceViewModel.class);
        viewModel.init(this);

        initViews();
        setupRecyclerView();
        setupObservers();
        loadExercises();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tv_title);
        tvCount = findViewById(R.id.tv_count);
        rvExercises = findViewById(R.id.rv_exercises);
        btnAdd = findViewById(R.id.btn_add);
        btnSave = findViewById(R.id.btn_save);

        tvTitle.setText(movementType.toUpperCase() + " MODULE");

        btnAdd.setOnClickListener(v -> showAddExerciseDialog());
        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void setupRecyclerView() {
        adapter = new ManageExercisesAdapter(
                exercises,
                exercise -> {
                    // Mark as modified
                    if (!modifiedExercises.contains(exercise)) {
                        modifiedExercises.add(exercise);
                    }
                },
                exercise -> {
                    // Mark for deletion
                    if (exercise.getPerformanceId() > 0) {
                        deletedExercises.add(exercise);
                    }
                    exercises.remove(exercise);
                    adapter.updateData(exercises);
                    updateCount();
                    Toast.makeText(this, "Exercise removed", Toast.LENGTH_SHORT).show();
                }
        );

        rvExercises.setLayoutManager(new LinearLayoutManager(this));
        rvExercises.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.getPerformances().observe(this, performances -> {
            if (performances != null) {
                exercises.clear();
                exercises.addAll(performances);
                adapter.updateData(exercises);
                updateCount();
            }
        });

        viewModel.getOperationSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Changes saved successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadExercises() {
        switch (movementType) {
            case "push":
                viewModel.getPushPerformances();
                break;
            case "pull":
                viewModel.getPullPerformances();
                break;
            case "legs":
                viewModel.getLegsPerformances();
                break;
        }
    }

    private void showAddExerciseDialog() {
        String[] exerciseNames = ExerciseRepository.getExerciseNamesByType(movementType);

        new AlertDialog.Builder(this)
                .setTitle("ADD EXERCISE")
                .setItems(exerciseNames, (dialog, which) -> {
                    String selectedName = exerciseNames[which];
                    showExerciseInputDialog(selectedName);
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    private void showExerciseInputDialog(String exerciseName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_exercise_input, null);

        com.google.android.material.textfield.TextInputEditText etReps =
                view.findViewById(R.id.et_reps);
        com.google.android.material.textfield.TextInputEditText etWeight =
                view.findViewById(R.id.et_weight);

        builder.setTitle(exerciseName)
                .setView(view)
                .setPositiveButton("ADD", (dialog, which) -> {
                    int reps = Integer.parseInt(etReps.getText().toString());
                    float weight = Float.parseFloat(etWeight.getText().toString());

                    // Create a new exercise performance (will be saved on batch update)
                    ExercisePerformance newExercise = new ExercisePerformance();
                    newExercise.setExerciseName(exerciseName);
                    newExercise.setReps(reps);
                    newExercise.setAddedWeight(weight);
                    newExercise.setPerformanceId(0); // 0 means new

                    exercises.add(newExercise);
                    modifiedExercises.add(newExercise);
                    adapter.updateData(exercises);
                    updateCount();
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }
    private void saveChanges() {
        if (modifiedExercises.isEmpty() && deletedExercises.isEmpty()) {
            Toast.makeText(this, "No changes to save", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSave.setEnabled(false);
        btnSave.setText("SAVING...");

        // Step 1: delete removed exercises first, then save
        if (!deletedExercises.isEmpty()) {
            deleteExercisesSequentially(0);
        } else {
            saveModifiedExercises();
        }
    }
    private void deleteExercisesSequentially(int index) {
        if (index >= deletedExercises.size()) {
            saveModifiedExercises();
            return;
        }

        ExercisePerformance exercise = deletedExercises.get(index);
        ApiService apiService = ApiClient.getApiService(ManageExercisesActivity.this);

        apiService.deleteExercise(exercise.getPerformanceId()).enqueue(new retrofit2.Callback<ExercisePerformance>() {
            @Override
            public void onResponse(retrofit2.Call<ExercisePerformance> call, retrofit2.Response<ExercisePerformance> response) {
                deleteExercisesSequentially(index + 1);
            }

            @Override
            public void onFailure(retrofit2.Call<ExercisePerformance> call, Throwable t) {
                deleteExercisesSequentially(index + 1);
            }
        });
    }
    private void saveModifiedExercises() {
        if (modifiedExercises.isEmpty()) {
            triggerRecalculation();
            return;
        }

        List<com.example.ranklifts_java.models.BatchUpdateRequest> requests = new ArrayList<>();
        for (ExercisePerformance ex : modifiedExercises) {
            requests.add(new com.example.ranklifts_java.models.BatchUpdateRequest(
                    ex.getPerformanceId() > 0 ? ex.getPerformanceId() : null,
                    ex.getExerciseName(),
                    ex.getAddedWeight(),
                    ex.getReps()
            ));
        }

        ApiService apiService = ApiClient.getApiService(ManageExercisesActivity.this);

        apiService.updateExerciseBatch(requests).enqueue(new retrofit2.Callback<List<ExercisePerformance>>() {
            @Override
            public void onResponse(retrofit2.Call<List<ExercisePerformance>> call, retrofit2.Response<List<ExercisePerformance>> response) {
                if (response.isSuccessful()) {
                    triggerRecalculation();
                } else {
                    btnSave.setEnabled(true);
                    btnSave.setText("SAVE CHANGES");
                    Toast.makeText(ManageExercisesActivity.this,
                            "Failed to save exercises: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<ExercisePerformance>> call, Throwable t) {
                btnSave.setEnabled(true);
                btnSave.setText("SAVE CHANGES");
                Toast.makeText(ManageExercisesActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void triggerRecalculation() {
        btnSave.setText("RECALCULATING...");
        ApiService apiService = ApiClient.getApiService(ManageExercisesActivity.this);

        apiService.recalculateAll().enqueue(new retrofit2.Callback<RecalculationResponse>() {
            @Override
            public void onResponse(retrofit2.Call<RecalculationResponse> call, retrofit2.Response<RecalculationResponse> response) {
                Toast.makeText(ManageExercisesActivity.this,
                        "Exercises saved and rankings updated!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(retrofit2.Call<RecalculationResponse> call, Throwable t) {
                Toast.makeText(ManageExercisesActivity.this,
                        "Saved but recalculation failed. Refresh home screen.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateCount() {
        tvCount.setText(exercises.size() + " ACTIVE ENTRIES");
    }
}