package com.example.ranklifts_java.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranklifts_java.R;
import com.example.ranklifts_java.viewmodel.ExerciseViewModel;

import java.util.List;

public class ExerciseEntryAdapter extends RecyclerView.Adapter<ExerciseEntryAdapter.ViewHolder> {

    private List<ExerciseViewModel.LocalExerciseEntry> entries;
    private OnEditClickListener editListener;
    private OnDeleteClickListener deleteListener;

    public interface OnEditClickListener {
        void onEdit(ExerciseViewModel.LocalExerciseEntry entry);
    }

    public interface OnDeleteClickListener {
        void onDelete(ExerciseViewModel.LocalExerciseEntry entry);
    }

    public ExerciseEntryAdapter(List<ExerciseViewModel.LocalExerciseEntry> entries,
                                OnEditClickListener editListener,
                                OnDeleteClickListener deleteListener) {
        this.entries = entries;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    public void updateData(List<ExerciseViewModel.LocalExerciseEntry> newEntries) {
        this.entries = newEntries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExerciseViewModel.LocalExerciseEntry entry = entries.get(position);

        holder.tvExerciseName.setText(entry.getExerciseName().toUpperCase());
        holder.etReps.setText(String.valueOf(entry.getReps()));
        holder.etWeight.setText(String.valueOf(entry.getAddedWeight()));

        if (entry.isBodyweight()) {
            holder.tvWeightLabel.setText("+KG (VEST)");
        } else {
            holder.tvWeightLabel.setText("KG");
        }

        holder.btnEdit.setOnClickListener(v -> editListener.onEdit(entry));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(entry));

        // Live updates
        holder.etReps.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && !holder.etReps.getText().toString().isEmpty()) {
                int reps = Integer.parseInt(holder.etReps.getText().toString());
                entry.setReps(reps);
            }
        });

        holder.etWeight.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && !holder.etWeight.getText().toString().isEmpty()) {
                float weight = Float.parseFloat(holder.etWeight.getText().toString());
                entry.setAddedWeight(weight);
            }
        });
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvExerciseName, tvWeightLabel;
        EditText etReps, etWeight;
        ImageButton btnEdit, btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tv_exercise_name);
            tvWeightLabel = itemView.findViewById(R.id.tv_weight_label);
            etReps = itemView.findViewById(R.id.et_reps);
            etWeight = itemView.findViewById(R.id.et_weight);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}