package com.example.savingproject;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.savingproject.databinding.ItemSavingBinding;
import java.util.List;

public class SavingsAdapter extends RecyclerView.Adapter<SavingsAdapter.ViewHolder> {
    private List<SavingsGoal> savingsGoals;

    public SavingsAdapter(List<SavingsGoal> savingsGoals) {
        this.savingsGoals = savingsGoals;
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
        holder.binding.goalAmount.setText(String.format("Progress: $%.2f / $%.2f", goal.getCurrentAmount(), goal.getTargetAmount()));
        
        int progress = (int) ((goal.getCurrentAmount() / goal.getTargetAmount()) * 100);
        holder.binding.goalProgress.setProgress(progress);
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
