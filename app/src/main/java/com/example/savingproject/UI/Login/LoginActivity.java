package com.example.savingproject.UI.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.savingproject.DATA.SessionManager;
import com.example.savingproject.UI.main.MainActivity;
import com.example.savingproject.databinding.ActivityLoginBinding;
import com.example.savingproject.util.UiAnimUtil;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
    private boolean loginInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager session = SessionManager.getInstance();
        if (session != null && session.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding.signInButton.setOnClickListener(v -> attemptLogin());

        binding.signUpButton.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));

        UiAnimUtil.fadeIn(binding.loginLogo, 0);
        UiAnimUtil.fadeIn(binding.loginTitle, 80);
        UiAnimUtil.fadeIn(binding.loginSubtitle, 140);
        UiAnimUtil.fadeInUp(binding.loginFormCard);
    }

    private void attemptLogin() {
        if (loginInProgress) return;

        String email = binding.emailEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        loginInProgress = true;
        binding.signInButton.setEnabled(false);

        viewModel.login(email, password).observe(this, success -> {
            loginInProgress = false;
            binding.signInButton.setEnabled(true);
            if (success) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Login Failed: Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
