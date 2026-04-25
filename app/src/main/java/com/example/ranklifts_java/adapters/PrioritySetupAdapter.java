package com.example.ranklifts_java.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranklifts_java.R;
import com.example.ranklifts_java.viewmodel.PriorityViewModel;

import java.util.HashMap;
import java.util.Map;

public class PrioritySetupAdapter extends RecyclerView.Adapter<PrioritySetupAdapter.ViewHolder> {

    private final String[] movements = {"push", "pull", "legs"};
    private final String[] titles = {"PUSH MODULE", "PULL MODULE", "LEGS MODULE"};
    private final String[] icons = {"💪", "🏋️", "🦵"};

    private Map<String, PriorityViewModel.PriorityData> priorities = new HashMap<>();
    private OnMovementClickListener listener;

    public interface OnMovementClickListener {
        void onMovementClick(String movementType);
    }

    public PrioritySetupAdapter(OnMovementClickListener listener) {
        this.listener = listener;
    }

    public void updatePriority(String movementType, PriorityViewModel.PriorityData data) {
        priorities.put(movementType, data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_priority_setup, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String movementType = movements[position];
        PriorityViewModel.PriorityData data = priorities.get(movementType);

        holder.tvIcon.setText(icons[position]);
        holder.tvTitle.setText(titles[position]);

        if (data != null && data.isComplete()) {
            holder.tvStatus.setText("CONFIGURED");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(R.color.cyan));

            // Display priority order
            String[] order = data.getPriorityOrder();
            holder.tvPriority1.setText("01 " + order[0]);
            holder.tvPriority2.setText("02 " + order[1]);
            holder.tvPriority3.setText("03 " + order[2]);
            holder.tvPriority4.setText("04 " + order[3]);

            holder.priorityContainer.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatus.setText("INITIALIZE");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(R.color.slate));
            holder.priorityContainer.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onMovementClick(movementType));
    }

    @Override
    public int getItemCount() {
        return movements.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIcon, tvTitle, tvStatus;
        TextView tvPriority1, tvPriority2, tvPriority3, tvPriority4;
        View priorityContainer;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIcon = itemView.findViewById(R.id.tv_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvPriority1 = itemView.findViewById(R.id.tv_priority_1);
            tvPriority2 = itemView.findViewById(R.id.tv_priority_2);
            tvPriority3 = itemView.findViewById(R.id.tv_priority_3);
            tvPriority4 = itemView.findViewById(R.id.tv_priority_4);
            priorityContainer = itemView.findViewById(R.id.priority_container);
        }
    }
}