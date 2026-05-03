package com.example.savingproject;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.savingproject.databinding.ItemSavingBinding;
import java.util.List;

public class SavingsAdapter extends RecyclerView.Adapter<SavingsAdapter.ViewHolder> {
    private List<SavingsGoal> savingsGoals;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SavingsGoal goal);
    }

    public SavingsAdapter(List<SavingsGoal> savingsGoals, OnItemClickListener listener) {
        this.savingsGoals = savingsGoals;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSavingBinding binding = ItemSavingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SavingsGoal goal = savingsGoals.get(position);
        holder.binding.goalName.setText(goal.getName());
        holder.binding.goalAmount.setText(holder.itemView.getContext().getString(R.string.progress_format, goal.getCurrentAmount(), goal.getTargetAmount()));
        
        int progress = (int) ((goal.getCurrentAmount() / goal.getTargetAmount()) * 100);
        holder.binding.goalProgress.setProgress(progress);
        holder.binding.goalPercentage.setText(String.format("%d%%", progress));

        if (goal.getTargetDate() != null && !goal.getTargetDate().isEmpty()) {
            holder.binding.goalDate.setText(holder.itemView.getContext().getString(R.string.date_format, goal.getTargetDate()));
            holder.binding.goalDate.setVisibility(android.view.View.VISIBLE);
        } else {
            holder.binding.goalDate.setVisibility(android.view.View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(goal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return savingsGoals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemSavingBinding binding;
        public ViewHolder(ItemSavingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
