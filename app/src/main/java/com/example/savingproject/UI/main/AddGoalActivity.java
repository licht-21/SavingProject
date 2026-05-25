package com.example.savingproject.UI.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.savingproject.MODEL.SavingsGoal;
import com.example.savingproject.R;
import com.example.savingproject.databinding.ActivityAddGoalBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AddGoalActivity extends AppCompatActivity {

    public static final String EXTRA_GOAL_ID = "goal_id";
    public static final String EXTRA_GOAL_NAME = "goal_name";
    public static final String EXTRA_TARGET = "target";
    public static final String EXTRA_CURRENT = "current";
    public static final String EXTRA_DUE_DATE = "due_date";

    private ActivityAddGoalBinding binding;
    private SavingsViewModel viewModel;
    private int editGoalId = -1;

    public static Intent newIntent(Context context, SavingsGoal goal) {
        Intent intent = new Intent(context, AddGoalActivity.class);
        if (goal != null) {
            intent.putExtra(EXTRA_GOAL_ID, goal.getId());
            intent.putExtra(EXTRA_GOAL_NAME, goal.getName());
            intent.putExtra(EXTRA_TARGET, goal.getTargetAmount());
            intent.putExtra(EXTRA_CURRENT, goal.getCurrentAmount());
            intent.putExtra(EXTRA_DUE_DATE, goal.getDueDate());
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddGoalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SavingsViewModel.class);

        editGoalId = getIntent().getIntExtra(EXTRA_GOAL_ID, -1);
        boolean editing = editGoalId > 0;

        if (editing) {
            binding.toolbarTitle.setText(R.string.edit_savings_goal);
            binding.goalNameInput.setText(getIntent().getStringExtra(EXTRA_GOAL_NAME));
            binding.targetAmountInput.setText(String.valueOf(getIntent().getDoubleExtra(EXTRA_TARGET, 0)));
            binding.startingAmountInput.setText(String.valueOf(getIntent().getDoubleExtra(EXTRA_CURRENT, 0)));
            String due = getIntent().getStringExtra(EXTRA_DUE_DATE);
            if (due != null) binding.dueDateInput.setText(due);
        } else {
            binding.toolbarTitle.setText(R.string.new_savings_goal);
        }

        // Setup Date Picker
        binding.dueDateInput.setOnClickListener(v -> showDatePicker());
        binding.dueDateLayout.setEndIconOnClickListener(v -> showDatePicker());
        // Also allow clicking on the TextInputLayout itself
        binding.dueDateLayout.setOnClickListener(v -> showDatePicker());

        binding.saveGoalButton.setOnClickListener(v -> saveGoal(editing));
        
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void showDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Due Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            binding.dueDateInput.setText(sdf.format(new Date(selection)));
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void saveGoal(boolean editing) {
        String name = binding.goalNameInput.getText().toString().trim();
        String targetStr = binding.targetAmountInput.getText().toString().trim();
        String currentStr = binding.startingAmountInput.getText().toString().trim();
        String dueDate = binding.dueDateInput.getText().toString().trim();

        if (name.isEmpty() || targetStr.isEmpty()) {
            Toast.makeText(this, R.string.fill_goal_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        double target;
        double current = 0;
        try {
            target = Double.parseDouble(targetStr);
            if (!currentStr.isEmpty()) {
                current = Double.parseDouble(currentStr);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.invalid_amount, Toast.LENGTH_SHORT).show();
            return;
        }

        if (target <= 0) {
            Toast.makeText(this, R.string.invalid_amount, Toast.LENGTH_SHORT).show();
            return;
        }

        String due = dueDate.isEmpty() ? null : dueDate;
        binding.saveGoalButton.setEnabled(false);

        if (editing) {
            viewModel.updateGoal(editGoalId, name, target, current, due).observe(this, goal -> {
                binding.saveGoalButton.setEnabled(true);
                finishSave(goal != null, true);
            });
        } else {
            viewModel.createGoal(name, target, current, due).observe(this, goal -> {
                binding.saveGoalButton.setEnabled(true);
                finishSave(goal != null, false);
            });
        }
    }

    private void finishSave(boolean success, boolean editing) {
        if (success) {
            Toast.makeText(this, editing ? R.string.goal_updated : R.string.goal_saved, Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, R.string.goal_save_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
