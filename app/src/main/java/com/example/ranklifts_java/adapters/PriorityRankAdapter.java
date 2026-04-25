package com.example.ranklifts_java.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranklifts_java.R;

import java.util.List;

public class PriorityRankAdapter extends RecyclerView.Adapter<PriorityRankAdapter.ViewHolder> {

    private String[] labels;
    private String[] priorities;
    private String[] descriptions;
    private OnPriorityClickListener listener;

    public interface OnPriorityClickListener {
        void onPriorityClick(int position);
    }

    public PriorityRankAdapter(String[] labels, String[] priorities, String[] descriptions, OnPriorityClickListener listener) {
        this.labels = labels;
        this.priorities = priorities;
        this.descriptions = descriptions;
        this.listener = listener;
    }

    public void updatePriorities(String[] newPriorities) {
        this.priorities = newPriorities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_priority_rank, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvRankNumber.setText("0" + (position + 1));
        holder.tvLabel.setText(labels[position]);
        holder.tvPriorityValue.setText(priorities[position].toUpperCase());
        holder.tvDescription.setText(descriptions[position]);

        holder.itemView.setOnClickListener(v -> listener.onPriorityClick(position));
    }

    @Override
    public int getItemCount() {
        return labels.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRankNumber, tvLabel, tvPriorityValue, tvDescription;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRankNumber = itemView.findViewById(R.id.tv_rank_number);
            tvLabel = itemView.findViewById(R.id.tv_label);
            tvPriorityValue = itemView.findViewById(R.id.tv_priority_value);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }
}