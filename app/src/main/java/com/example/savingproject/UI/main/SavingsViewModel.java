package com.example.savingproject.UI.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.savingproject.DATA.SavingsRepository;
import com.example.savingproject.MODEL.SavingsGoal;
import com.example.savingproject.MODEL.UserSettings;

import java.util.List;

public class SavingsViewModel extends ViewModel {
    private final SavingsRepository repository = new SavingsRepository();
    private final MutableLiveData<List<SavingsGoal>> activeSavings = new MutableLiveData<>();
    private final MutableLiveData<List<SavingsGoal>> archivedSavings = new MutableLiveData<>();

    public LiveData<List<SavingsGoal>> getActiveSavings() {
        return activeSavings;
    }

    public LiveData<List<SavingsGoal>> getArchivedSavings() {
        return archivedSavings;
    }

    public void refreshActiveSavings() {
        repository.fetchSavings(false, activeSavings);
    }

    public void refreshArchivedSavings() {
        repository.fetchSavings(true, archivedSavings);
    }

    public void loadSettings(MutableLiveData<UserSettings> target) {
        repository.fetchSettings(target);
    }

    public LiveData<UserSettings> saveSettings(boolean darkMode, String dateFormat) {
        return repository.saveSettings(darkMode, dateFormat);
    }

    public LiveData<SavingsGoal> createGoal(String name, double targetAmount, double currentAmount, String dueDate) {
        return repository.createGoal(name, targetAmount, currentAmount, dueDate);
    }

    public LiveData<SavingsGoal> updateGoal(int goalId, String name, double targetAmount, double currentAmount, String dueDate) {
        return repository.updateGoal(goalId, name, targetAmount, currentAmount, dueDate);
    }

    public LiveData<Boolean> deleteGoal(int goalId) {
        return repository.deleteGoal(goalId);
    }

    public LiveData<SavingsGoal> archiveGoal(int goalId) {
        return repository.archiveGoal(goalId);
    }

    public LiveData<SavingsGoal> deposit(int goalId, double amount) {
        return repository.deposit(goalId, amount);
    }
}
