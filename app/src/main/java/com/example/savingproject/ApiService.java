package com.example.savingproject;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/savings")
    Call<List<SavingsGoal>> getSavings();

    @POST("/savings")
    Call<SavingsGoal> addSaving(@Body SavingsGoal goal);
}
