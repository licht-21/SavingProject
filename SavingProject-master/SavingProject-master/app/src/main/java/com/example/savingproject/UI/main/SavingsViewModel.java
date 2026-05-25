package com.example.savingproject.UI.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.savingproject.DATA.SavingsRepository;
import com.example.savingproject.MODEL.Deposit;
import com.example.savingproject.MODEL.SavingsGoal;
import com.example.savingproject.MODEL.SavingsSummary;
import com.example.savingproject.MODEL.UserSettings;

import java.util.List;

public class SavingsViewModel extends ViewModel {
    public static final String SORT_NEWEST = "newest";
    public static final String SORT_NAME = "name";
    public static final String SORT_DUE_DATE = "due_date";
    public static final String SORT_PROGRESS = "progress";

    private final SavingsRepository repository = new SavingsRepository();
    private final MutableLiveData<List<SavingsGoal>> activeSavings = new MutableLiveData<>();
    private final MutableLiveData<List<SavingsGoal>> archivedSavings = new MutableLiveData<>();
    private final MutableLiveData<SavingsSummary> summary = new MutableLiveData<>();
    private String activeSort = SORT_NEWEST;

    public LiveData<List<SavingsGoal>> getActiveSavings() {
        return activeSavings;
    }

    public LiveData<List<SavingsGoal>> getArchivedSavings() {
        return archivedSavings;
    }

    public LiveData<SavingsSummary> getSummary() {
        return summary;
    }

    public String getActiveSort() {
        return activeSort;
    }

    public void setActiveSort(String sort) {
        activeSort = sort;
    }

    public void refreshActiveSavings() {
        repository.fetchSavings(false, activeSort, activeSavings);
        repository.fetchSummary(summary);
    }

    public void refreshArchivedSavings() {
        repository.fetchSavings(true, SORT_NEWEST, archivedSavings);
    }

    public void loadSettings(MutableLiveData<UserSettings> target) {
        repository.fetchSettings(target);
    }

    public void loadDeposits(int goalId, MutableLiveData<List<Deposit>> target) {
        repository.fetchDeposits(goalId, target);
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

    public LiveData<SavingsGoal> duplicateGoal(int goalId) {
        return repository.duplicateGoal(goalId);
    }

    public LiveData<SavingsGoal> deposit(int goalId, double amount) {
        return repository.deposit(goalId, amount);
    }
}
