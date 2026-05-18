package com.example.savingproject.UI.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savingproject.MODEL.SavingsGoal;
import com.example.savingproject.R;
import com.example.savingproject.databinding.ItemSavingBinding;
import com.example.savingproject.util.DateFormatUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SavingsAdapter extends RecyclerView.Adapter<SavingsAdapter.ViewHolder> {

    public interface OnGoalActionListener {
        void onGoalClick(SavingsGoal goal);
        void onEditGoal(SavingsGoal goal);
        void onDeleteGoal(SavingsGoal goal);
        void onArchiveGoal(SavingsGoal goal);
    }

    public enum DisplayMode {
        ACTIVE,
        ARCHIVED
    }

    private List<SavingsGoal> savingsGoals = new ArrayList<>();
    private final OnGoalActionListener listener;
    private final DisplayMode displayMode;

    public SavingsAdapter(OnGoalActionListener listener, DisplayMode displayMode) {
        this.listener = listener;
        this.displayMode = displayMode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSavingBinding binding = ItemSavingBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SavingsGoal goal = savingsGoals.get(position);
        int percent = goal.computeProgressPercent();
        boolean archived = displayMode == DisplayMode.ARCHIVED || goal.isArchived();

        holder.binding.goalName.setText(goal.getName());
        holder.binding.goalPercent.setText(String.format(Locale.getDefault(), "%d%%", percent));
        holder.binding.goalAmount.setText(String.format(Locale.getDefault(),
                "₱%,.2f / ₱%,.2f", goal.getCurrentAmount(), goal.getTargetAmount()));
        holder.binding.goalProgress.setProgress(percent);
        holder.binding.goalProgress.setProgressTintList(
                ColorStateList.valueOf(resolveProgressColor(holder, percent)));

        if (archived && goal.getArchivedAt() != null && !goal.getArchivedAt().isEmpty()) {
            holder.binding.goalSubtitle.setVisibility(View.VISIBLE);
            holder.binding.goalSubtitle.setText(holder.itemView.getContext().getString(
                    R.string.archived_on, DateFormatUtil.formatDisplay(goal.getArchivedAt())));
        } else if (!archived && goal.getDueDate() != null && !goal.getDueDate().isEmpty()) {
            holder.binding.goalSubtitle.setVisibility(View.VISIBLE);
            holder.binding.goalSubtitle.setText(holder.itemView.getContext().getString(
                    R.string.due_by, DateFormatUtil.formatDisplay(goal.getDueDate())));
        } else {
            holder.binding.goalSubtitle.setVisibility(View.GONE);
        }

        if (archived) {
            holder.binding.goalHint.setVisibility(View.GONE);
            holder.binding.goalMenuButton.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(null);
        } else {
            holder.binding.goalHint.setVisibility(View.VISIBLE);
            holder.binding.goalMenuButton.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onGoalClick(goal);
            });
            holder.binding.goalMenuButton.setOnClickListener(v -> showPopup(holder, goal, percent));
        }
    }

    private void showPopup(ViewHolder holder, SavingsGoal goal, int percent) {
        PopupMenu menu = new PopupMenu(holder.itemView.getContext(), holder.binding.goalMenuButton);
        menu.inflate(R.menu.goal_options_menu);
        menu.getMenu().findItem(R.id.action_archive).setVisible(percent >= 100);
        menu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_edit) {
                if (listener != null) listener.onEditGoal(goal);
                return true;
            }
            if (id == R.id.action_delete) {
                if (listener != null) listener.onDeleteGoal(goal);
                return true;
            }
            if (id == R.id.action_archive) {
                if (listener != null) listener.onArchiveGoal(goal);
                return true;
            }
            return false;
        });
        menu.show();
    }

    private int resolveProgressColor(ViewHolder holder, int percent) {
        int colorRes;
        if (percent >= 100) {
            colorRes = R.color.progress_green;
        } else if (percent >= 76) {
            colorRes = R.color.progress_yellow_green;
        } else if (percent >= 31) {
            colorRes = R.color.progress_yellow;
        } else {
            colorRes = R.color.progress_red;
        }
        return ContextCompat.getColor(holder.itemView.getContext(), colorRes);
    }

    @Override
    public int getItemCount() {
        return savingsGoals.size();
    }

    public void updateData(List<SavingsGoal> newGoals) {
        this.savingsGoals = newGoals != null ? newGoals : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemSavingBinding binding;

        public ViewHolder(ItemSavingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
