package com.example.savingproject.UI.Login;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.savingproject.databinding.ActivitySignupBinding;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding.signUpButton.setOnClickListener(v -> {
            String name = binding.nameEditText.getText().toString();
            String email = binding.emailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.signup(name, email, password).observe(this, success -> {
                if (success) {
                    Toast.makeText(SignUpActivity.this, "Sign Up Successful! Please Log In.", Toast.LENGTH_LONG).show();
                    finish(); // Go back to LoginActivity
                } else {
                    Toast.makeText(SignUpActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.loginTextView.setOnClickListener(v -> finish());
    }
}
