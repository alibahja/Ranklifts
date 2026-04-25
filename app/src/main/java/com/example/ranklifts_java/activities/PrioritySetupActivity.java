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
import com.example.ranklifts_java.adapters.PrioritySetupAdapter;
import com.example.ranklifts_java.viewmodel.PriorityViewModel;

public class PrioritySetupActivity extends AppCompatActivity {

    private RecyclerView rvPriorities;
    private Button btnContinue;
    private TextView tvProgress;

    private PriorityViewModel viewModel;
    private PrioritySetupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priority_setup);

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(PriorityViewModel.class);
// Remove viewModel.init(this)

        initViews();
        setupRecyclerView();
        setupObservers();

        // Load existing priorities
        viewModel.loadPriorities();
    }

    private void initViews() {
        rvPriorities = findViewById(R.id.rv_priorities);
        btnContinue = findViewById(R.id.btn_continue);
        tvProgress = findViewById(R.id.tv_progress);

        btnContinue.setOnClickListener(v -> {
            if (viewModel.hasAllPriorities()) {
                // Save priorities to server before continuing
                viewModel.saveAllPriorities();
            } else {
                Toast.makeText(this, "Please complete all 3 movement priorities", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new PrioritySetupAdapter(movementType -> {
            Intent intent = new Intent(PrioritySetupActivity.this, MovementPriorityActivity.class);
            intent.putExtra("movement_type", movementType);
            startActivity(intent);
        });

        rvPriorities.setLayoutManager(new LinearLayoutManager(this));
        rvPriorities.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.getPushPriority().observe(this, priority -> {
            adapter.updatePriority("push", priority);
            updateProgress();
        });

        viewModel.getPullPriority().observe(this, priority -> {
            adapter.updatePriority("pull", priority);
            updateProgress();
        });

        viewModel.getLegsPriority().observe(this, priority -> {
            adapter.updatePriority("legs", priority);
            updateProgress();
        });

        viewModel.getSaveSuccess().observe(this, success -> {
            if (success) {
                startActivity(new Intent(PrioritySetupActivity.this, HomeActivity.class));
                finish();
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            btnContinue.setEnabled(!isLoading);
            if (isLoading) {
                btnContinue.setText("SAVING...");
            } else {
                btnContinue.setText("FINALIZE PROTOCOLS");
            }
        });
    }

    private void updateProgress() {
        int completed = 0;
        if (viewModel.getPushPriority().getValue() != null && viewModel.getPushPriority().getValue().isComplete()) completed++;
        if (viewModel.getPullPriority().getValue() != null && viewModel.getPullPriority().getValue().isComplete()) completed++;
        if (viewModel.getLegsPriority().getValue() != null && viewModel.getLegsPriority().getValue().isComplete()) completed++;

        tvProgress.setText(completed + " / 3 MODULES CONFIGURED");
        btnContinue.setEnabled(completed == 3);
    }
}