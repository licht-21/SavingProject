package com.example.savingproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SavingsRepository {
    private ApiService apiService;
    private static final String BASE_URL = "http://10.0.2.2:5000/"; // Default for Android Emulator to host localhost

    public SavingsRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
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
