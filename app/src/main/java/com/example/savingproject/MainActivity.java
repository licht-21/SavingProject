package com.example.savingproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.savingproject.databinding.ActivityMainBinding;
import com.example.savingproject.databinding.DialogSavingBinding;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SavingsViewModel viewModel;
    private SavingsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // Setup RecyclerView
        binding.savingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupAdapter(new ArrayList<>());

        // Initialize ViewModel and observe data
        viewModel = new ViewModelProvider(this).get(SavingsViewModel.class);
        viewModel.getSavings().observe(this, savings -> {
            if (savings != null && !savings.isEmpty()) {
                setupAdapter(savings);
                binding.savingsRecyclerView.setVisibility(View.VISIBLE);
                binding.emptyView.setVisibility(View.GONE);
            } else {
                binding.savingsRecyclerView.setVisibility(View.GONE);
                binding.emptyView.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });

        binding.addGoalFab.setOnClickListener(v -> showSavingDialog(null));
    }

    private void setupAdapter(java.util.List<SavingsGoal> savings) {
        adapter = new SavingsAdapter(savings, this::showSavingDialog);
        binding.savingsRecyclerView.setAdapter(adapter);
    }

    private void showSavingDialog(SavingsGoal goal) {
        DialogSavingBinding dialogBinding = DialogSavingBinding.inflate(LayoutInflater.from(this));
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogBinding.getRoot())
                .create();

        if (goal != null) {
            dialogBinding.dialogTitle.setText(R.string.dialog_edit_title);
            dialogBinding.editGoalName.setText(goal.getName());
            dialogBinding.editGoalTarget.setText(String.valueOf(goal.getTargetAmount()));
            dialogBinding.editGoalCurrent.setText(String.valueOf(goal.getCurrentAmount()));
            dialogBinding.editGoalDate.setText(goal.getTargetDate());
            dialogBinding.btnDelete.setVisibility(View.VISIBLE);
            dialogBinding.btnDelete.setOnClickListener(v -> {
                viewModel.deleteSaving(goal.getId());
                dialog.dismiss();
            });
        }

        dialogBinding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.btnSave.setOnClickListener(v -> {
            String name = dialogBinding.editGoalName.getText().toString();
            String targetStr = dialogBinding.editGoalTarget.getText().toString();
            String currentStr = dialogBinding.editGoalCurrent.getText().toString();
            String date = dialogBinding.editGoalDate.getText().toString();

            if (name.isEmpty() || targetStr.isEmpty()) {
                Toast.makeText(this, R.string.error_fields_required, Toast.LENGTH_SHORT).show();
                return;
            }

            double target = Double.parseDouble(targetStr);
            double current = currentStr.isEmpty() ? 0 : Double.parseDouble(currentStr);

            if (goal == null) {
                viewModel.addSaving(new SavingsGoal(name, target, current, date));
            } else {
                goal.setName(name);
                goal.setTargetAmount(target);
                goal.setCurrentAmount(current);
                goal.setTargetDate(date);
                viewModel.updateSaving(goal);
            }
            dialog.dismiss();
        });

        dialog.show();
    }
}
