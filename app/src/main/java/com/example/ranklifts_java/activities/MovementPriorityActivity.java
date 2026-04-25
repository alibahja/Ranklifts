package com.example.ranklifts_java.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranklifts_java.R;
import com.example.ranklifts_java.adapters.PriorityRankAdapter;
import com.example.ranklifts_java.viewmodel.PriorityViewModel;

public class MovementPriorityActivity extends AppCompatActivity {

    private String movementType;
    private TextView tvTitle, tvSubtitle;
    private RecyclerView rvPriorities;
    private Button btnSave;

    private PriorityViewModel viewModel;
    private PriorityRankAdapter adapter;
    private String[] selectedPriorities;

    private final String[] categories = {"Compound", "Calisthenics", "PrimaryIsolation", "PureIsolation"};
    private final String[] labels = {"PRIMARY STRENGTH", "SECONDARY LOAD", "TERTIARY SUPPORT", "AUXILIARY VOLUME"};
    private final String[] descriptions = {
            "High-impact multi-joint movements (Bench, Squat, Deadlift)",
            "Bodyweight mastery & control (Push-ups, Pull-ups, Dips)",
            "Heavy targeted muscle engagement (Curls, Tricep Press)",
            "Refinement and accessory volume (Calf raises, Face pulls)"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_priority);

        movementType = getIntent().getStringExtra("movement_type");
        if (movementType == null) {
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(PriorityViewModel.class);
// Remove viewModel.init(this)

        initViews();
        loadCurrentPriorities();
        setupRecyclerView();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tv_title);
        tvSubtitle = findViewById(R.id.tv_subtitle);
        rvPriorities = findViewById(R.id.rv_priorities);
        btnSave = findViewById(R.id.btn_save);

        String title = movementType.toUpperCase() + " PROTOCOL";
        tvTitle.setText(title);
        tvSubtitle.setText("ESTABLISH HIERARCHY SEQUENCE");

        btnSave.setOnClickListener(v -> savePriorities());
    }

    private void loadCurrentPriorities() {
        PriorityViewModel.PriorityData data = null;

        switch (movementType) {
            case "push":
                data = viewModel.getPushPriority().getValue();
                break;
            case "pull":
                data = viewModel.getPullPriority().getValue();
                break;
            case "legs":
                data = viewModel.getLegsPriority().getValue();
                break;
        }

        if (data != null && data.isComplete()) {
            selectedPriorities = data.getPriorityOrder();
        } else {
            selectedPriorities = new String[]{"Compound", "Calisthenics", "PrimaryIsolation", "PureIsolation"};
        }
    }

    private void setupRecyclerView() {
        adapter = new PriorityRankAdapter(labels, selectedPriorities, descriptions, position -> {
            showCategoryPicker(position);
        });

        rvPriorities.setLayoutManager(new LinearLayoutManager(this));
        rvPriorities.setAdapter(adapter);
    }

    private void showCategoryPicker(int position) {
        String[] categoryNames = categories;
        int currentIndex = -1;

        for (int i = 0; i < categoryNames.length; i++) {
            if (categoryNames[i].equals(selectedPriorities[position])) {
                currentIndex = i;
                break;
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("SELECT CATEGORY")
                .setSingleChoiceItems(categoryNames, currentIndex, (dialog, which) -> {
                    String newCategory = categoryNames[which];

                    // Check if already used elsewhere
                    int existingIndex = -1;
                    for (int i = 0; i < selectedPriorities.length; i++) {
                        if (i != position && selectedPriorities[i].equals(newCategory)) {
                            existingIndex = i;
                            break;
                        }
                    }

                    if (existingIndex != -1) {
                        // Swap priorities
                        String temp = selectedPriorities[position];
                        selectedPriorities[position] = selectedPriorities[existingIndex];
                        selectedPriorities[existingIndex] = temp;
                    } else {
                        selectedPriorities[position] = newCategory;
                    }

                    adapter.updatePriorities(selectedPriorities);
                    dialog.dismiss();
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    private void savePriorities() {
        viewModel.updatePriority(
                movementType,
                selectedPriorities[0],
                selectedPriorities[1],
                selectedPriorities[2],
                selectedPriorities[3]
        );

        finish();
    }
}