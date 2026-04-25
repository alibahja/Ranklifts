package com.example.ranklifts_java.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ranklifts_java.R;
import com.example.ranklifts_java.viewmodel.RankingsViewModel;

import java.util.List;

public class MovementRankAdapter extends RecyclerView.Adapter<MovementRankAdapter.ViewHolder> {

    private List<RankingsViewModel.MovementRankItem> items;

    public MovementRankAdapter(List<RankingsViewModel.MovementRankItem> items) {
        this.items = items;
    }

    public void updateData(List<RankingsViewModel.MovementRankItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movement_rank, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RankingsViewModel.MovementRankItem item = items.get(position);

        holder.tvMovementType.setText(item.getMovementTypeUpper());
        holder.tvCategoryRank.setText(item.getCategoryRank());
        holder.tvCategoryRank.setTextColor(item.getRankColor());
        holder.tvCategoryScore.setText(String.format("%.1f", item.getCategoryScore()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovementType, tvCategoryRank, tvCategoryScore;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovementType = itemView.findViewById(R.id.tv_movement_type);
            tvCategoryRank = itemView.findViewById(R.id.tv_category_rank);
            tvCategoryScore = itemView.findViewById(R.id.tv_category_score);
        }
    }
}
