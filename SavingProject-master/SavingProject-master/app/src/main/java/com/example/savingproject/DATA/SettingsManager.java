package com.example.savingproject.DATA;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.savingproject.MODEL.UserSettings;

public class SettingsManager {

    private static final String PREFS = "app_settings";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_DATE_FORMAT = "date_format";

    public static final String DATE_DD_MM_YYYY = "DD/MM/YYYY";
    public static final String DATE_YYYY_MM_DD = "YYYY-MM-DD";

    private static SettingsManager instance;
    private final SharedPreferences prefs;

    private SettingsManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new SettingsManager(context);
        }
    }

    public static SettingsManager getInstance() {
        return instance;
    }

    public void applyFromServer(UserSettings settings) {
        if (settings == null) return;
        prefs.edit()
                .putBoolean(KEY_DARK_MODE, settings.isDarkMode())
                .putString(KEY_DATE_FORMAT, settings.getDateFormat())
                .apply();
        applyTheme();
    }

    public void saveLocal(boolean darkMode, String dateFormat) {
        prefs.edit()
                .putBoolean(KEY_DARK_MODE, darkMode)
                .putString(KEY_DATE_FORMAT, dateFormat)
                .apply();
        applyTheme();
    }

    public boolean isDarkMode() {
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }

    public String getDateFormat() {
        return prefs.getString(KEY_DATE_FORMAT, DATE_DD_MM_YYYY);
    }

    public void applyTheme() {
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }
}
