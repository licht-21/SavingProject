package com.example.savingproject.UI.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.savingproject.DATA.SessionManager;
import com.example.savingproject.DATA.SettingsManager;
import com.example.savingproject.MODEL.UserSettings;
import com.example.savingproject.R;
import com.example.savingproject.UI.Login.LoginActivity;
import com.example.savingproject.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private SavingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager session = SessionManager.getInstance();
        if (session == null || !session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SavingsViewModel.class);
        BottomNavHelper.setup(this, binding.bottomNav, R.id.nav_settings);

        loadSettingsIntoUi();

        binding.saveSettingsButton.setOnClickListener(v -> saveSettings());
        binding.logoutButton.setOnClickListener(v -> logout());
    }

    private void loadSettingsIntoUi() {
        SettingsManager settings = SettingsManager.getInstance();
        binding.darkModeSwitch.setChecked(settings.isDarkMode());
        if (SettingsManager.DATE_YYYY_MM_DD.equals(settings.getDateFormat())) {
            binding.dateFormatYyyymmdd.setChecked(true);
        } else {
            binding.dateFormatDdmm.setChecked(true);
        }

        MutableLiveData<UserSettings> settingsLive = new MutableLiveData<>();
        settingsLive.observe(this, loaded -> {
            if (loaded != null) {
                binding.darkModeSwitch.setChecked(loaded.isDarkMode());
                if (SettingsManager.DATE_YYYY_MM_DD.equals(loaded.getDateFormat())) {
                    binding.dateFormatYyyymmdd.setChecked(true);
                } else {
                    binding.dateFormatDdmm.setChecked(true);
                }
            }
        });
        viewModel.loadSettings(settingsLive);
    }

    private void saveSettings() {
        boolean darkMode = binding.darkModeSwitch.isChecked();
        String dateFormat = binding.dateFormatYyyymmdd.isChecked()
                ? SettingsManager.DATE_YYYY_MM_DD
                : SettingsManager.DATE_DD_MM_YYYY;

        binding.saveSettingsButton.setEnabled(false);
        viewModel.saveSettings(darkMode, dateFormat).observe(this, saved -> {
            binding.saveSettingsButton.setEnabled(true);
            if (saved != null) {
                Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT).show();
            } else {
                SettingsManager.getInstance().saveLocal(darkMode, dateFormat);
                Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        SessionManager.getInstance().clearSession();
        startActivity(new Intent(this, LoginActivity.class));
        finishAffinity();
    }
}
