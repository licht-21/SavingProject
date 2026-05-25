package com.example.savingproject.UI.Login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.savingproject.DATA.SavingsRepository;

public class LoginViewModel extends ViewModel {
    private SavingsRepository repository;

    public LoginViewModel() {
        repository = new SavingsRepository();
    }

    public LiveData<Boolean> login(String email, String password) {
        return repository.login(email, password);
    }

    public LiveData<Boolean> signup(String name, String email, String password) {
        return repository.signup(name, email, password);
    }
}
