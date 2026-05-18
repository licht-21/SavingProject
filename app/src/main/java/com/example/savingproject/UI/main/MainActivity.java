package com.example.savingproject.UI.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.savingproject.DATA.SessionManager;
import com.example.savingproject.MODEL.SavingsGoal;
import com.example.savingproject.R;
import com.example.savingproject.UI.Login.LoginActivity;
import com.example.savingproject.UI.adapter.SavingsAdapter;
import com.example.savingproject.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SavingsAdapter.OnGoalActionListener {

    private ActivityMainBinding binding;
    private SavingsViewModel viewModel;
    private SavingsAdapter adapter;

    private final ActivityResultLauncher<Intent> goalFormLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    viewModel.refreshActiveSavings();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!ensureLoggedIn()) return;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.savingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SavingsAdapter(this, SavingsAdapter.DisplayMode.ACTIVE);
        binding.savingsRecyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(SavingsViewModel.class);
        viewModel.getActiveSavings().observe(this, this::renderSavings);

        binding.addGoalFab.setOnClickListener(v -> openAddGoal());
        BottomNavHelper.setup(this, binding.bottomNav, R.id.nav_active);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.refreshActiveSavings();
            viewModel.loadSettings(new androidx.lifecycle.MutableLiveData<>());
        }
    }

    private boolean ensureLoggedIn() {
        SessionManager session = SessionManager.getInstance();
        if (session == null || !session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return false;
        }
        return true;
    }

    private void renderSavings(List<SavingsGoal> savings) {
        if (savings == null) savings = new ArrayList<>();
        adapter.updateData(savings);
        boolean empty = savings.isEmpty();
        binding.emptyStateText.setVisibility(empty ? View.VISIBLE : View.GONE);
        binding.savingsRecyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    private void openAddGoal() {
        goalFormLauncher.launch(AddGoalActivity.newIntent(this, null));
    }

    @Override
    public void onGoalClick(SavingsGoal goal) {
        showDepositDialog(goal);
    }

    @Override
    public void onEditGoal(SavingsGoal goal) {
        goalFormLauncher.launch(AddGoalActivity.newIntent(this, goal));
    }

    @Override
    public void onDeleteGoal(SavingsGoal goal) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_goal)
                .setMessage(R.string.delete_confirm)
                .setPositiveButton(R.string.delete_goal, (d, w) ->
                        viewModel.deleteGoal(goal.getId()).observe(this, ok -> {
                            if (Boolean.TRUE.equals(ok)) {
                                Toast.makeText(this, R.string.delete_success, Toast.LENGTH_SHORT).show();
                                viewModel.refreshActiveSavings();
                            } else {
                                Toast.makeText(this, R.string.delete_failed, Toast.LENGTH_SHORT).show();
                            }
                        }))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onArchiveGoal(SavingsGoal goal) {
        viewModel.archiveGoal(goal.getId()).observe(this, archived -> {
            if (archived != null) {
                Toast.makeText(this, R.string.archive_success, Toast.LENGTH_SHORT).show();
                viewModel.refreshActiveSavings();
            } else {
                Toast.makeText(this, R.string.archive_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDepositDialog(SavingsGoal goal) {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint(R.string.deposit_hint);

        new AlertDialog.Builder(this)
                .setTitle(R.string.deposit_title)
                .setMessage(goal.getName())
                .setView(input)
                .setPositiveButton(R.string.deposit_confirm, (dialog, which) -> {
                    String amountStr = input.getText().toString().trim();
                    if (amountStr.isEmpty()) {
                        Toast.makeText(this, R.string.invalid_amount, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        double amount = Double.parseDouble(amountStr);
                        if (amount <= 0) {
                            Toast.makeText(this, R.string.invalid_amount, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        submitDeposit(goal.getId(), amount);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, R.string.invalid_amount, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void submitDeposit(int goalId, double amount) {
        viewModel.deposit(goalId, amount).observe(this, updated -> {
            if (updated != null) {
                if (updated.isArchived()) {
                    Toast.makeText(this, R.string.deposit_archived, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.deposit_success, Toast.LENGTH_SHORT).show();
                }
                viewModel.refreshActiveSavings();
            } else {
                Toast.makeText(this, R.string.deposit_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
