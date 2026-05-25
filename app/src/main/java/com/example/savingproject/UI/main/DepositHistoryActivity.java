package com.example.savingproject.UI.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.savingproject.MODEL.Deposit;
import com.example.savingproject.MODEL.SavingsGoal;
import com.example.savingproject.R;
import com.example.savingproject.UI.adapter.DepositAdapter;
import com.example.savingproject.databinding.ActivityDepositHistoryBinding;

import java.util.ArrayList;
import java.util.List;

public class DepositHistoryActivity extends AppCompatActivity {

    private static final String EXTRA_GOAL_ID = "goal_id";
    private static final String EXTRA_GOAL_NAME = "goal_name";

    private ActivityDepositHistoryBinding binding;
    private SavingsViewModel viewModel;
    private DepositAdapter adapter;

    public static Intent newIntent(Context context, SavingsGoal goal) {
        Intent intent = new Intent(context, DepositHistoryActivity.class);
        intent.putExtra(EXTRA_GOAL_ID, goal.getId());
        intent.putExtra(EXTRA_GOAL_NAME, goal.getName());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDepositHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        String goalName = getIntent().getStringExtra(EXTRA_GOAL_NAME);
        if (goalName != null) {
            binding.toolbarSubtitle.setText(goalName);
            binding.toolbarSubtitle.setVisibility(View.VISIBLE);
        } else {
            binding.toolbarSubtitle.setVisibility(View.GONE);
        }

        int goalId = getIntent().getIntExtra(EXTRA_GOAL_ID, -1);
        adapter = new DepositAdapter();
        binding.depositsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.depositsRecyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(SavingsViewModel.class);
        androidx.lifecycle.MutableLiveData<List<Deposit>> depositsLive = new androidx.lifecycle.MutableLiveData<>();
        depositsLive.observe(this, this::renderDeposits);
        viewModel.loadDeposits(goalId, depositsLive);
    }

    private void renderDeposits(List<Deposit> deposits) {
        if (deposits == null) deposits = new ArrayList<>();
        adapter.updateData(deposits);
        boolean empty = deposits.isEmpty();
        binding.emptyStateText.setVisibility(empty ? View.VISIBLE : View.GONE);
        binding.depositsRecyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
    }
}
