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
import com.example.ranklifts_java.models.ExercisePerformance;

import java.util.List;

public class ManageExercisesAdapter extends RecyclerView.Adapter<ManageExercisesAdapter.ViewHolder> {

    private List<ExercisePerformance> exercises;
    private OnExerciseChangeListener changeListener;
    private OnExerciseDeleteListener deleteListener;

    public interface OnExerciseChangeListener {
        void onExerciseChanged(ExercisePerformance exercise);
    }

    public interface OnExerciseDeleteListener {
        void onExerciseDeleted(ExercisePerformance exercise);
    }

    public ManageExercisesAdapter(List<ExercisePerformance> exercises,
                                  OnExerciseChangeListener changeListener,
                                  OnExerciseDeleteListener deleteListener) {
        this.exercises = exercises;
        this.changeListener = changeListener;
        this.deleteListener = deleteListener;
    }

    public void updateData(List<ExercisePerformance> newExercises) {
        this.exercises = newExercises;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manage_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExercisePerformance exercise = exercises.get(position);

        holder.tvExerciseName.setText(exercise.getExerciseName());
        holder.etReps.setText(String.valueOf(exercise.getReps()));
        holder.etWeight.setText(String.valueOf(exercise.getAddedWeight()));

        // Save changes when focus lost
        holder.etReps.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && !holder.etReps.getText().toString().isEmpty()) {
                int reps = Integer.parseInt(holder.etReps.getText().toString());
                exercise.setReps(reps);
                changeListener.onExerciseChanged(exercise);
            }
        });

        holder.etWeight.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && !holder.etWeight.getText().toString().isEmpty()) {
                float weight = Float.parseFloat(holder.etWeight.getText().toString());
                exercise.setAddedWeight(weight);
                changeListener.onExerciseChanged(exercise);
            }
        });

        holder.btnDelete.setOnClickListener(v -> deleteListener.onExerciseDeleted(exercise));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvExerciseName;
        EditText etReps, etWeight;
        ImageButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tv_exercise_name);
            etReps = itemView.findViewById(R.id.et_reps);
            etWeight = itemView.findViewById(R.id.et_weight);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
