package com.example.savingproject.DATA;

import com.example.savingproject.MODEL.SavingsGoal;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

// --- Helper Model Classes for JSON Mapping ---
class RegisterRequest {
    private String name;
    private String email;
    private String password;

    public RegisterRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}

class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

class AuthResponse {
    private String status;
    private String message;
    private int user_id;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public int getUserId() { return user_id; }
}

class HealthStatusResponse {
    private String status;
    private String message;

    public String getStatus() { return status; }
    public String getMessage() { return message; }
}

// --- Updated API Interface ---
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
