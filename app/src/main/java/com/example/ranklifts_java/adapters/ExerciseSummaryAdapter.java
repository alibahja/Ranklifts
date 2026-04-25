package com.example.ranklifts_java.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranklifts_java.R;
import com.example.ranklifts_java.viewmodel.ExerciseViewModel;

import java.util.List;

public class ExerciseSummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PUSH = 0;
    private static final int TYPE_PULL = 1;
    private static final int TYPE_LEGS = 2;

    private List<ExerciseViewModel.LocalExerciseEntry> pushExercises;
    private List<ExerciseViewModel.LocalExerciseEntry> pullExercises;
    private List<ExerciseViewModel.LocalExerciseEntry> legsExercises;

    public void updateData(List<ExerciseViewModel.LocalExerciseEntry> push,
                           List<ExerciseViewModel.LocalExerciseEntry> pull,
                           List<ExerciseViewModel.LocalExerciseEntry> legs) {
        this.pushExercises = push;
        this.pullExercises = pull;
        this.legsExercises = legs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int pushSize = pushExercises != null ? pushExercises.size() : 0;
        int pullSize = pullExercises != null ? pullExercises.size() : 0;

        if (position == 0) return TYPE_PUSH;
        if (position == 1 + pushSize) return TYPE_PULL;
        if (position == 1 + pushSize + 1 + pullSize) return TYPE_LEGS;

        // Inside push section
        if (position > 0 && position <= pushSize) return TYPE_PUSH;
        // Inside pull section
        if (position > pushSize + 1 && position <= pushSize + 1 + pullSize) return TYPE_PULL;
        // Inside legs section
        return TYPE_LEGS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_PUSH && (pushExercises == null || pushExercises.isEmpty())) {
            return new EmptyViewHolder(inflater.inflate(R.layout.item_summary_empty, parent, false));
        } else if (viewType == TYPE_PULL && (pullExercises == null || pullExercises.isEmpty())) {
            return new EmptyViewHolder(inflater.inflate(R.layout.item_summary_empty, parent, false));
        } else if (viewType == TYPE_LEGS && (legsExercises == null || legsExercises.isEmpty())) {
            return new EmptyViewHolder(inflater.inflate(R.layout.item_summary_empty, parent, false));
        }

        View view = inflater.inflate(R.layout.item_summary_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SectionViewHolder) {
            int pushSize = pushExercises != null ? pushExercises.size() : 0;
            int pullSize = pullExercises != null ? pullExercises.size() : 0;

            String title;
            List<ExerciseViewModel.LocalExerciseEntry> exercises;

            if (position == 0) {
                title = "PUSH PROTOCOL";
                exercises = pushExercises;
            } else if (position == 1 + pushSize) {
                title = "PULL PROTOCOL";
                exercises = pullExercises;
            } else {
                title = "LEGS PROTOCOL";
                exercises = legsExercises;
            }

            SectionViewHolder sectionHolder = (SectionViewHolder) holder;
            sectionHolder.tvTitle.setText(title);
            sectionHolder.tvCount.setText((exercises != null ? exercises.size() : 0) + " ACTIVE ENTRIES");

            // Build exercise list string
            StringBuilder sb = new StringBuilder();
            if (exercises != null) {
                for (int i = 0; i < Math.min(exercises.size(), 5); i++) {
                    ExerciseViewModel.LocalExerciseEntry entry = exercises.get(i);
                    sb.append(entry.getExerciseName().toUpperCase())
                            .append("  ")
                            .append(entry.getReps()).append("R // ")
                            .append((int)entry.getAddedWeight()).append("KG\n");
                }
                if (exercises.size() > 5) {
                    sb.append("... and ").append(exercises.size() - 5).append(" more");
                }
            }
            sectionHolder.tvExercises.setText(sb.toString());
        }
    }

    @Override
    public int getItemCount() {
        int pushSize = pushExercises != null ? pushExercises.size() : 0;
        int pullSize = pullExercises != null ? pullExercises.size() : 0;
        int legsSize = legsExercises != null ? legsExercises.size() : 0;

        return (pushSize > 0 ? 1 + pushSize : 0) +
                (pullSize > 0 ? 1 + pullSize : 0) +
                (legsSize > 0 ? 1 + legsSize : 0);
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCount, tvExercises;

        SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_section_title);
            tvCount = itemView.findViewById(R.id.tv_section_count);
            tvExercises = itemView.findViewById(R.id.tv_exercises_list);
        }
    }

    static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}