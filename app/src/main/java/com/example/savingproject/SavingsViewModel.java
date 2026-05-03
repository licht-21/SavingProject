package com.example.savingproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

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
