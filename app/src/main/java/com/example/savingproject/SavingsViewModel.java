package com.example.savingproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavingsViewModel extends ViewModel {
    private SavingsRepository repository;
    private LiveData<List<SavingsGoal>> savings;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SavingsViewModel() {
        repository = new SavingsRepository();
        refreshSavings();
    }

    public void refreshSavings() {
        savings = repository.getSavings();
    }

    public LiveData<List<SavingsGoal>> getSavings() {
        return savings;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void addSaving(SavingsGoal goal) {
        repository.addSaving(goal, new Callback<SavingsGoal>() {
            @Override
            public void onResponse(Call<SavingsGoal> call, Response<SavingsGoal> response) {
                if (response.isSuccessful()) refreshSavings();
                else errorMessage.setValue("Failed to add saving");
            }
            @Override
            public void onFailure(Call<SavingsGoal> call, Throwable t) {
                errorMessage.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void updateSaving(SavingsGoal goal) {
        repository.updateSaving(goal, new Callback<SavingsGoal>() {
            @Override
            public void onResponse(Call<SavingsGoal> call, Response<SavingsGoal> response) {
                if (response.isSuccessful()) refreshSavings();
                else errorMessage.setValue("Failed to update saving");
            }
            @Override
            public void onFailure(Call<SavingsGoal> call, Throwable t) {
                errorMessage.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void deleteSaving(int id) {
        repository.deleteSaving(id, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) refreshSavings();
                else errorMessage.setValue("Failed to delete saving");
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.setValue("Error: " + t.getMessage());
            }
        });
    }
}
