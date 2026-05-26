package com.example.savingproject.UI.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.savingproject.UI.main.MainActivity;
import com.example.savingproject.databinding.ActivitySignupBinding;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private LoginViewModel viewModel;
    private boolean signupInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding.signUpButton.setOnClickListener(v -> attemptSignUp());

        binding.loginTextView.setOnClickListener(v -> finish());
    }

    private void attemptSignUp() {
        if (signupInProgress) return;

        String name = binding.nameEditText.getText().toString().trim();
        String email = binding.emailEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        signupInProgress = true;
        binding.signUpButton.setEnabled(false);

        viewModel.signup(name, email, password).observe(this, success -> {
            signupInProgress = false;
            binding.signUpButton.setEnabled(true);
            if (success) {
                Toast.makeText(SignUpActivity.this, "Account created!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
