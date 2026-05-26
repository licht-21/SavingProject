package com.example.savingproject;

import android.app.Application;

import com.example.savingproject.DATA.SessionManager;
import com.example.savingproject.DATA.SettingsManager;

public class SavingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SessionManager.init(this);
        SettingsManager.init(this);
        SettingsManager.getInstance().applyTheme();
    }
}
