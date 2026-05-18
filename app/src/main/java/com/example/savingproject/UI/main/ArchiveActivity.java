package com.example.savingproject.UI.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.savingproject.DATA.SessionManager;
import com.example.savingproject.MODEL.SavingsGoal;
import com.example.savingproject.R;
import com.example.savingproject.UI.Login.LoginActivity;
import com.example.savingproject.UI.adapter.SavingsAdapter;
import com.example.savingproject.databinding.ActivityArchiveBinding;

import java.util.ArrayList;
import java.util.List;

public class ArchiveActivity extends AppCompatActivity implements SavingsAdapter.OnGoalActionListener {

    private ActivityArchiveBinding binding;
    private SavingsViewModel viewModel;
    private SavingsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager session = SessionManager.getInstance();
        if (session == null || !session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        binding = ActivityArchiveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.savingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SavingsAdapter(this, SavingsAdapter.DisplayMode.ARCHIVED);
        binding.savingsRecyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(SavingsViewModel.class);
        viewModel.getArchivedSavings().observe(this, this::renderSavings);

        BottomNavHelper.setup(this, binding.bottomNav, R.id.nav_archive);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.refreshArchivedSavings();
        }
    }

    private void renderSavings(List<SavingsGoal> savings) {
        if (savings == null) savings = new ArrayList<>();
        adapter.updateData(savings);
        boolean empty = savings.isEmpty();
        binding.emptyStateText.setVisibility(empty ? View.VISIBLE : View.GONE);
        binding.savingsRecyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onGoalClick(SavingsGoal goal) { }

    @Override
    public void onEditGoal(SavingsGoal goal) { }

    @Override
    public void onDeleteGoal(SavingsGoal goal) { }

    @Override
    public void onArchiveGoal(SavingsGoal goal) { }
}
