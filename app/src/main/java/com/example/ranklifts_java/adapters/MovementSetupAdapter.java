package com.example.ranklifts_java.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranklifts_java.R;

import java.util.HashMap;
import java.util.Map;

public class MovementSetupAdapter extends RecyclerView.Adapter<MovementSetupAdapter.ViewHolder> {

    private final String[] movements = {"push", "pull", "legs"};
    private final String[] titles = {"PUSH EXERCISES", "PULL EXERCISES", "LEGS EXERCISES"};
    private final String[] descriptions = {
            "CHEST // SHOULDERS // TRICEPS",
            "BACK // BICEPS // REAR DELTS",
            "QUADS // HAMS // GLUTES // CALVES"
    };

    private Map<String, Integer> exerciseCounts = new HashMap<>();
    private OnMovementClickListener listener;

    public interface OnMovementClickListener {
        void onMovementClick(String movementType);
    }

    public MovementSetupAdapter(OnMovementClickListener listener) {
        this.listener = listener;
        exerciseCounts.put("push", 0);
        exerciseCounts.put("pull", 0);
        exerciseCounts.put("legs", 0);
    }

    public void updateCount(String movementType, int count) {
        exerciseCounts.put(movementType, count);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movement_setup, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String movementType = movements[position];
        int count = exerciseCounts.getOrDefault(movementType, 0);
        boolean hasExercises = count > 0;

        holder.tvTitle.setText(titles[position]);
        holder.tvDescription.setText(descriptions[position]);

        if (hasExercises) {
            holder.tvStatus.setText(count + " UNIT" + (count != 1 ? "S" : "") + " ACTIVE");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(R.color.cyan));
            holder.cardBorder.setBackgroundResource(R.drawable.cyber_card_active);
        } else {
            holder.tvStatus.setText("READY FOR INPUT");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(R.color.slate));
            holder.cardBorder.setBackgroundResource(R.drawable.cyber_card_border);
        }

        holder.itemView.setOnClickListener(v -> listener.onMovementClick(movementType));
    }

    @Override
    public int getItemCount() {
        return movements.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvStatus;
        View cardBorder;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvStatus = itemView.findViewById(R.id.tv_status);
            cardBorder = itemView;
        }
    }
}