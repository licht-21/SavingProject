package com.example.savingproject.DATA;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.savingproject.MODEL.SavingsGoal;
import com.example.savingproject.MODEL.UserSettings;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavingsRepository {

    private final ApiService apiService;

    public SavingsRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    private void persistUserFromResponse(AuthResponse body) {
        if (body == null || body.getUser() == null) return;
        AuthResponse.UserData user = body.getUser();
        SessionManager manager = SessionManager.getInstance();
        if (manager != null) {
            manager.saveSession(user.getId(), user.getEmail(), user.getUsername());
        }
    }

    public LiveData<Boolean> login(String email, String password) {
        MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
        apiService.loginUser(new LoginRequest(email, password)).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && "success".equalsIgnoreCase(response.body().getStatus())) {
                    persistUserFromResponse(response.body());
                    loginResult.setValue(true);
                } else {
                    loginResult.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                loginResult.setValue(false);
            }
        });
        return loginResult;
    }

    public LiveData<Boolean> signup(String name, String email, String password) {
        MutableLiveData<Boolean> signupResult = new MutableLiveData<>();
        apiService.registerUser(new RegisterRequest(name, email, password)).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && "success".equalsIgnoreCase(response.body().getStatus())) {
                    persistUserFromResponse(response.body());
                    signupResult.setValue(true);
                } else {
                    signupResult.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                signupResult.setValue(false);
            }
        });
        return signupResult;
    }

    public void fetchSavings(boolean archived, MutableLiveData<List<SavingsGoal>> result) {
        apiService.getSavings(archived ? 1 : 0).enqueue(new Callback<List<SavingsGoal>>() {
            @Override
            public void onResponse(Call<List<SavingsGoal>> call, Response<List<SavingsGoal>> response) {
                result.setValue(response.isSuccessful() && response.body() != null ? response.body() : null);
            }

            @Override
            public void onFailure(Call<List<SavingsGoal>> call, Throwable t) {
                result.setValue(null);
            }
        });
    }

    public void fetchSettings(MutableLiveData<UserSettings> result) {
        apiService.getSettings().enqueue(new Callback<UserSettings>() {
            @Override
            public void onResponse(Call<UserSettings> call, Response<UserSettings> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SettingsManager.getInstance().applyFromServer(response.body());
                    result.setValue(response.body());
                } else {
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserSettings> call, Throwable t) {
                result.setValue(null);
            }
        });
    }

    public LiveData<UserSettings> saveSettings(boolean darkMode, String dateFormat) {
        MutableLiveData<UserSettings> liveData = new MutableLiveData<>();
        apiService.updateSettings(new SettingsRequest(darkMode, dateFormat)).enqueue(new Callback<UserSettings>() {
            @Override
            public void onResponse(Call<UserSettings> call, Response<UserSettings> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SettingsManager.getInstance().applyFromServer(response.body());
                    liveData.setValue(response.body());
                } else {
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UserSettings> call, Throwable t) {
                liveData.setValue(null);
            }
        });
        return liveData;
    }

    public LiveData<SavingsGoal> createGoal(String name, double targetAmount, double currentAmount, String dueDate) {
        MutableLiveData<SavingsGoal> result = new MutableLiveData<>();
        apiService.createSavingsGoal(new CreateGoalRequest(name, targetAmount, currentAmount, dueDate))
                .enqueue(goalCallback(result));
        return result;
    }

    public LiveData<SavingsGoal> updateGoal(int goalId, String name, double targetAmount, double currentAmount, String dueDate) {
        MutableLiveData<SavingsGoal> result = new MutableLiveData<>();
        apiService.updateSavingsGoal(goalId, new UpdateGoalRequest(name, targetAmount, currentAmount, dueDate))
                .enqueue(goalCallback(result));
        return result;
    }

    public LiveData<Boolean> deleteGoal(int goalId) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        apiService.deleteSavingsGoal(goalId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                result.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                result.setValue(false);
            }
        });
        return result;
    }

    public LiveData<SavingsGoal> archiveGoal(int goalId) {
        MutableLiveData<SavingsGoal> result = new MutableLiveData<>();
        apiService.archiveSavingsGoal(goalId).enqueue(goalCallback(result));
        return result;
    }

    public LiveData<SavingsGoal> deposit(int goalId, double amount) {
        MutableLiveData<SavingsGoal> result = new MutableLiveData<>();
        apiService.depositToGoal(goalId, new DepositRequest(amount)).enqueue(goalCallback(result));
        return result;
    }

    private Callback<SavingsGoal> goalCallback(MutableLiveData<SavingsGoal> result) {
        return new Callback<SavingsGoal>() {
            @Override
            public void onResponse(Call<SavingsGoal> call, Response<SavingsGoal> response) {
                result.setValue(response.isSuccessful() ? response.body() : null);
            }

            @Override
            public void onFailure(Call<SavingsGoal> call, Throwable t) {
                result.setValue(null);
            }
        };
    }
}
