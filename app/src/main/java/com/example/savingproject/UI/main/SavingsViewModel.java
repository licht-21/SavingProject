package com.example.savingproject.UI.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

import com.example.savingproject.DATA.SavingsRepository;
import com.example.savingproject.MODEL.SavingsGoal;

public class SavingsViewModel extends ViewModel {
    private SavingsRepository repository;
    private LiveData<List<SavingsGoal>> savings;

    public SavingsViewModel() {
        repository = new SavingsRepository();
        savings = repository.getSavings();
    }

    public LiveData<List<SavingsGoal>> getSavings() {
        return savings;
    }
}
