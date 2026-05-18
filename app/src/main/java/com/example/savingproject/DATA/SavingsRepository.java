package com.example.savingproject.DATA;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.savingproject.MODEL.SavingsGoal;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavingsRepository {

    private final ApiService apiService;

    public SavingsRepository() {
        this.apiService = RetrofitClient.INSTANCE.getApiService();
    }

    public LiveData<String> testBackendConnection() {
        MutableLiveData<String> testResult = new MutableLiveData<>();

        apiService.checkBackendHealth().enqueue(new Callback<HealthStatusResponse>() {
            @Override
            public void onResponse(Call<HealthStatusResponse> call, Response<HealthStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    HealthStatusResponse data = response.body();
                    testResult.setValue(data.getMessage());
                } else {
                    testResult.setValue("Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<HealthStatusResponse> call, Throwable t) {
                testResult.setValue("Connection Failed: " + t.getMessage());
            }
        });

        return testResult;
    }

    public LiveData<Boolean> login(String email, String password) {
        MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
        LoginRequest request = new LoginRequest(email, password);

        apiService.loginUser(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loginResult.setValue("success".equalsIgnoreCase(response.body().getStatus()));
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
        RegisterRequest request = new RegisterRequest(name, email, password);

        apiService.registerUser(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    signupResult.setValue("success".equalsIgnoreCase(response.body().getStatus()));
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

    public LiveData<List<SavingsGoal>> getSavings() {
        MutableLiveData<List<SavingsGoal>> data = new MutableLiveData<>();
        apiService.getSavings().enqueue(new Callback<List<SavingsGoal>>() {
            @Override
            public void onResponse(Call<List<SavingsGoal>> call, Response<List<SavingsGoal>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<SavingsGoal>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
