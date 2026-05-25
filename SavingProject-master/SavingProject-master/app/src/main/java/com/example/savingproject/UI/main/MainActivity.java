package com.example.savingproject.UI.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.savingproject.MODEL.SavingsSummary;
import com.example.savingproject.R;
import com.example.savingproject.UI.Login.LoginActivity;
import com.example.savingproject.UI.adapter.SavingsAdapter;
import com.example.savingproject.databinding.ActivityMainBinding;
import com.example.savingproject.databinding.DialogDepositBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        binding.savingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SavingsAdapter(this, SavingsAdapter.DisplayMode.ACTIVE);
        binding.savingsRecyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(SavingsViewModel.class);
        viewModel.getActiveSavings().observe(this, this::renderSavings);
        viewModel.getSummary().observe(this, this::renderSummary);

        binding.swipeRefresh.setOnRefreshListener(() -> {
            viewModel.refreshActiveSavings();
            binding.swipeRefresh.setRefreshing(false);
        });

        binding.addGoalFab.setOnClickListener(v -> openAddGoal());
        BottomNavHelper.setup(this, binding.bottomNav, R.id.nav_active);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_newest) {
            applySort(SavingsViewModel.SORT_NEWEST);
            return true;
        }
        if (id == R.id.sort_name) {
            applySort(SavingsViewModel.SORT_NAME);
            return true;
        }
        if (id == R.id.sort_due_date) {
            applySort(SavingsViewModel.SORT_DUE_DATE);
            return true;
        }
        if (id == R.id.sort_progress) {
            applySort(SavingsViewModel.SORT_PROGRESS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void applySort(String sort) {
        viewModel.setActiveSort(sort);
        viewModel.refreshActiveSavings();
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

    private void renderSummary(SavingsSummary summary) {
        if (summary == null) return;
        binding.summaryCard.summarySavedText.setText(getString(R.string.summary_saved,
                summary.getTotalSaved()));
        binding.summaryCard.summaryTargetText.setText(getString(R.string.summary_target,
                summary.getTotalTarget()));
        binding.summaryCard.summaryGoalsText.setText(getString(R.string.summary_goals,
                summary.getActiveGoalCount()));
        if (summary.getOverdueCount() > 0) {
            binding.summaryCard.summaryOverdueText.setVisibility(View.VISIBLE);
            binding.summaryCard.summaryOverdueText.setText(getString(R.string.summary_overdue,
                    summary.getOverdueCount()));
        } else {
            binding.summaryCard.summaryOverdueText.setVisibility(View.GONE);
        }
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
    public void onViewHistory(SavingsGoal goal) {
        startActivity(DepositHistoryActivity.newIntent(this, goal));
    }

    @Override
    public void onDuplicateGoal(SavingsGoal goal) {
        viewModel.duplicateGoal(goal.getId()).observe(this, duplicated -> {
            if (duplicated != null) {
                Toast.makeText(this, R.string.duplicate_success, Toast.LENGTH_SHORT).show();
                viewModel.refreshActiveSavings();
            } else {
                Toast.makeText(this, R.string.duplicate_failed, Toast.LENGTH_SHORT).show();
            }
        });
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
        DialogDepositBinding dialogBinding = DialogDepositBinding.inflate(getLayoutInflater());

        dialogBinding.quick100Button.setOnClickListener(v ->
                incrementDepositAmount(dialogBinding.depositAmountInput, 100));
        dialogBinding.quick500Button.setOnClickListener(v ->
                incrementDepositAmount(dialogBinding.depositAmountInput, 500));
        dialogBinding.quick1000Button.setOnClickListener(v ->
                incrementDepositAmount(dialogBinding.depositAmountInput, 1000));

        new AlertDialog.Builder(this)
                .setTitle(R.string.deposit_title)
                .setMessage(goal.getName())
                .setView(dialogBinding.getRoot())
                .setPositiveButton(R.string.deposit_confirm, (dialog, which) -> {
                    String amountStr = dialogBinding.depositAmountInput.getText().toString().trim();
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

    private void incrementDepositAmount(EditText input, double increment) {
        String current = input.getText().toString().trim();
        double value = 0;
        if (!current.isEmpty()) {
            try {
                value = Double.parseDouble(current);
            } catch (NumberFormatException ignored) {
                value = 0;
            }
        }
        double total = value + increment;
        if (total == Math.floor(total) && !Double.isInfinite(total)) {
            input.setText(String.valueOf((long) total));
        } else {
            input.setText(String.valueOf(total));
        }
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
