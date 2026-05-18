package com.example.savingproject.DATA;

import com.example.savingproject.MODEL.SavingsGoal;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

// --- Helper Model Classes for JSON Mapping ---

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

class AuthResponse {
    private String status;
    private String message;
    private UserData user;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
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

class HealthStatusResponse {
    private String status;
    private String message;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
}

public interface ApiService {

    @POST("api/register")
    Call<AuthResponse> registerUser(@Body RegisterRequest request);

    @POST("api/login")
    Call<AuthResponse> loginUser(@Body LoginRequest request);

    @GET("api/health")
    Call<HealthStatusResponse> checkBackendHealth();

    @GET("api/savings")
    Call<List<SavingsGoal>> getSavings();
}
