package com.example.ranklifts_java.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranklifts_java.R;
import com.example.ranklifts_java.adapters.ExerciseEntryAdapter;
import com.example.ranklifts_java.repository.ExerciseRepository;
import com.example.ranklifts_java.viewmodel.ExerciseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MovementExercisesActivity extends AppCompatActivity {

    private String movementType;
    private TextView tvTitle, tvSubtitle;
    private RecyclerView rvExercises;
    private FloatingActionButton fabAdd;
    private Button btnContinue;

    private ExerciseViewModel viewModel;
    private ExerciseEntryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_exercises);

        movementType = getIntent().getStringExtra("movement_type");
        if (movementType == null) {
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(ExerciseViewModel.class);

        initViews();
        setupRecyclerView();
        loadExercises();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tv_title);
        tvSubtitle = findViewById(R.id.tv_subtitle);
        rvExercises = findViewById(R.id.rv_exercises);
        fabAdd = findViewById(R.id.fab_add);
        btnContinue = findViewById(R.id.btn_continue);

        String title = movementType.toUpperCase() + " MODULE";
        tvTitle.setText(title);
        tvSubtitle.setText("RECORD PERFORMANCE METRICS");

        fabAdd.setOnClickListener(v -> showExercisePickerDialog());
        btnContinue.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new ExerciseEntryAdapter(
                viewModel.getExercises(movementType),
                entry -> showEditExerciseDialog(entry),
                entry -> {
                    viewModel.removeExercise(movementType, entry.getId());
                    refreshList();
                }
        );

        rvExercises.setLayoutManager(new LinearLayoutManager(this));
        rvExercises.setAdapter(adapter);
    }

    private void loadExercises() {
        refreshList();
    }

    private void refreshList() {
        adapter.updateData(viewModel.getExercises(movementType));
    }

    private void showExercisePickerDialog() {
        String[] exerciseNames = ExerciseRepository.getExerciseNamesByType(movementType);

        new AlertDialog.Builder(this)
                .setTitle("SELECT EXERCISE")
                .setItems(exerciseNames, (dialog, which) -> {
                    String selectedName = exerciseNames[which];
                    ExerciseRepository.Exercise exercise = ExerciseRepository.getExerciseByName(selectedName);
                    if (exercise != null) {
                        showAddExerciseDialog(exercise);
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    private void showAddExerciseDialog(ExerciseRepository.Exercise exercise) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_exercise_input, null);

        EditText etReps = view.findViewById(R.id.et_reps);
        EditText etWeight = view.findViewById(R.id.et_weight);
        TextView tvHint = view.findViewById(R.id.tv_hint);

        if (exercise.isBodyweight()) {
            tvHint.setText("Added weight in kg (e.g., weighted vest)");
        } else {
            tvHint.setText("Weight lifted in kg");
        }

        builder.setTitle(exercise.getName())
                .setView(view)
                .setPositiveButton("ADD", (dialog, which) -> {
                    int reps = Integer.parseInt(etReps.getText().toString());
                    float weight = Float.parseFloat(etWeight.getText().toString());

                    String id = System.currentTimeMillis() + "";
                    ExerciseViewModel.LocalExerciseEntry entry =
                            new ExerciseViewModel.LocalExerciseEntry(id, exercise.getId(), exercise.getName(),
                                    reps, weight, exercise.isBodyweight(), null);

                    viewModel.addExercise(movementType, entry);
                    refreshList();
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    private void showEditExerciseDialog(ExerciseViewModel.LocalExerciseEntry entry) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_exercise_input, null);

        EditText etReps = view.findViewById(R.id.et_reps);
        EditText etWeight = view.findViewById(R.id.et_weight);

        etReps.setText(String.valueOf(entry.getReps()));
        etWeight.setText(String.valueOf(entry.getAddedWeight()));

        builder.setTitle(entry.getExerciseName())
                .setView(view)
                .setPositiveButton("UPDATE", (dialog, which) -> {
                    int reps = Integer.parseInt(etReps.getText().toString());
                    float weight = Float.parseFloat(etWeight.getText().toString());

                    viewModel.updateExercise(movementType, entry.getId(), reps, weight);
                    refreshList();
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }
}