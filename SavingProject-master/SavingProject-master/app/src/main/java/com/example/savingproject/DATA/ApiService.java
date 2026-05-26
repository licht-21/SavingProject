package com.example.savingproject.DATA;

import com.example.savingproject.MODEL.Deposit;
import com.example.savingproject.MODEL.SavingsGoal;
import com.example.savingproject.MODEL.SavingsSummary;
import com.example.savingproject.MODEL.UserSettings;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

class RegisterRequest {
    private String username;
    private String email;
    private String password;

    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

class CreateGoalRequest {
    private String name;
    @SerializedName("targetAmount")
    private double targetAmount;
    @SerializedName("currentAmount")
    private double currentAmount;
    @SerializedName("dueDate")
    private String dueDate;

    public CreateGoalRequest(String name, double targetAmount, double currentAmount, String dueDate) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.dueDate = dueDate;
    }
}

class UpdateGoalRequest {
    private String name;
    @SerializedName("targetAmount")
    private double targetAmount;
    @SerializedName("currentAmount")
    private double currentAmount;
    @SerializedName("dueDate")
    private String dueDate;

    public UpdateGoalRequest(String name, double targetAmount, double currentAmount, String dueDate) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.dueDate = dueDate;
    }
}

class DepositRequest {
    private double amount;

    public DepositRequest(double amount) {
        this.amount = amount;
    }
}

class SettingsRequest {
    @SerializedName("darkMode")
    private boolean darkMode;
    @SerializedName("dateFormat")
    private String dateFormat;

    public SettingsRequest(boolean darkMode, String dateFormat) {
        this.darkMode = darkMode;
        this.dateFormat = dateFormat;
    }
}

class AuthResponse {
    private String status;
    private String message;
    private UserData user;

    public String getStatus() { return status; }
    public UserData getUser() { return user; }

    public static class UserData {
        private int id;
        private String username;
        private String email;

        public int getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
    }
}

public interface ApiService {

    @POST("api/register")
    Call<AuthResponse> registerUser(@Body RegisterRequest request);

    @POST("api/login")
    Call<AuthResponse> loginUser(@Body LoginRequest request);

    @GET("api/savings")
    Call<List<SavingsGoal>> getSavings(@Query("archived") int archived, @Query("sort") String sort);

    @GET("api/savings/summary")
    Call<SavingsSummary> getSavingsSummary();

    @GET("api/savings/{id}/deposits")
    Call<List<Deposit>> getDeposits(@Path("id") int goalId);

    @POST("api/savings/{id}/duplicate")
    Call<SavingsGoal> duplicateSavingsGoal(@Path("id") int goalId);

    @POST("api/savings")
    Call<SavingsGoal> createSavingsGoal(@Body CreateGoalRequest request);

    @PUT("api/savings/{id}")
    Call<SavingsGoal> updateSavingsGoal(@Path("id") int goalId, @Body UpdateGoalRequest request);

    @DELETE("api/savings/{id}")
    Call<Void> deleteSavingsGoal(@Path("id") int goalId);

    @POST("api/savings/{id}/archive")
    Call<SavingsGoal> archiveSavingsGoal(@Path("id") int goalId);

    @POST("api/savings/{id}/deposit")
    Call<SavingsGoal> depositToGoal(@Path("id") int goalId, @Body DepositRequest request);

    @GET("api/settings")
    Call<UserSettings> getSettings();

    @PUT("api/settings")
    Call<UserSettings> updateSettings(@Body SettingsRequest request);
}
