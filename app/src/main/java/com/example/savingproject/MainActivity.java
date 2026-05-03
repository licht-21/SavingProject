package com.example.savingproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.savingproject.databinding.ActivityMainBinding;
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

        // Setup RecyclerView
        binding.savingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SavingsAdapter(new ArrayList<>());
        binding.savingsRecyclerView.setAdapter(adapter);

        // Initialize ViewModel and observe data
        viewModel = new ViewModelProvider(this).get(SavingsViewModel.class);
        viewModel.getSavings().observe(this, savings -> {
            if (savings != null) {
                adapter = new SavingsAdapter(savings);
                binding.savingsRecyclerView.setAdapter(adapter);
            }
        });
    }
}
