package com.example.savingproject.UI.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.savingproject.UI.adapter.SavingsAdapter;
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

        // 1. Setup RecyclerView Layout Manager
        binding.savingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 2. Initialize the adapter exactly ONCE with an empty initial list
        adapter = new SavingsAdapter(new ArrayList<>());
        binding.savingsRecyclerView.setAdapter(adapter);

        // 3. Initialize ViewModel using ViewModelProvider
        viewModel = new ViewModelProvider(this).get(SavingsViewModel.class);

        // 4. Observe the data stream reactively
        viewModel.getSavings().observe(this, savings -> {
            if (savings != null) {
                // Simply update the list content smoothly without destroying the adapter engine
                adapter.updateData(savings);
            }
        });
    }
}
