package com.example.savingproject;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/savings")
    Call<List<SavingsGoal>> getSavings();

    @POST("/savings")
    Call<SavingsGoal> addSaving(@Body SavingsGoal goal);

    @PUT("/savings/{id}")
    Call<SavingsGoal> updateSaving(@Path("id") int id, @Body SavingsGoal goal);

    @DELETE("/savings/{id}")
    Call<Void> deleteSaving(@Path("id") int id);
}
