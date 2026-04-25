package com.example.ranklifts_java.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranklifts_java.R;
import com.example.ranklifts_java.adapters.MovementSetupAdapter;
import com.example.ranklifts_java.viewmodel.ExerciseViewModel;

public class ExerciseSetupActivity extends AppCompatActivity {

    private RecyclerView rvMovements;
    private Button btnContinue;
    private TextView tvProgress;

    private ExerciseViewModel exerciseViewModel;
    private MovementSetupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_setup);

        exerciseViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(ExerciseViewModel.class);
        initViews();
        setupObservers();

        // Load exercise counts
        exerciseViewModel.loadExerciseCounts();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh counts when returning from MovementExercisesActivity
        exerciseViewModel.loadExerciseCounts();
    }

    private void initViews() {
        rvMovements = findViewById(R.id.rv_movements);
        btnContinue = findViewById(R.id.btn_continue);
        tvProgress = findViewById(R.id.tv_progress);

        adapter = new MovementSetupAdapter(movementType -> {
            Intent intent = new Intent(ExerciseSetupActivity.this, MovementExercisesActivity.class);
            intent.putExtra("movement_type", movementType);
            startActivity(intent);
        });

        rvMovements.setLayoutManager(new LinearLayoutManager(this));
        rvMovements.setAdapter(adapter);

        btnContinue.setOnClickListener(v -> {
            if (exerciseViewModel.hasAllExercises()) {
                // Go to summary/submit BEFORE priorities
                startActivity(new Intent(ExerciseSetupActivity.this, ExerciseSummaryActivity.class));
            } else {
                Toast.makeText(this, "Please complete all 3 movement modules", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupObservers() {
        exerciseViewModel.getPushCount().observe(this, count -> {
            adapter.updateCount("push", count);
            updateProgress();
        });

        exerciseViewModel.getPullCount().observe(this, count -> {
            adapter.updateCount("pull", count);
            updateProgress();
        });

        exerciseViewModel.getLegsCount().observe(this, count -> {
            adapter.updateCount("legs", count);
            updateProgress();
        });
    }

    private void updateProgress() {
        int completed = 0;
        if (exerciseViewModel.getPushCount().getValue() != null && exerciseViewModel.getPushCount().getValue() > 0) completed++;
        if (exerciseViewModel.getPullCount().getValue() != null && exerciseViewModel.getPullCount().getValue() > 0) completed++;
        if (exerciseViewModel.getLegsCount().getValue() != null && exerciseViewModel.getLegsCount().getValue() > 0) completed++;

        tvProgress.setText(completed + " / 3 MODULES INITIALIZED");
        btnContinue.setEnabled(completed == 3);
    }
}
