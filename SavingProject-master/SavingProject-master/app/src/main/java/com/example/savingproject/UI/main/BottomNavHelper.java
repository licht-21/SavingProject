package com.example.savingproject.UI.main;

import android.app.Activity;
import android.content.Intent;

import com.example.savingproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public final class BottomNavHelper {

    private BottomNavHelper() {}

    public static void setup(Activity activity, BottomNavigationView nav, int selectedItemId) {
        nav.setSelectedItemId(selectedItemId);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_active) {
                if (!(activity instanceof MainActivity)) {
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                }
                return true;
            }
            if (id == R.id.nav_archive) {
                if (!(activity instanceof ArchiveActivity)) {
                    activity.startActivity(new Intent(activity, ArchiveActivity.class));
                    activity.finish();
                }
                return true;
            }
            if (id == R.id.nav_settings) {
                if (!(activity instanceof SettingsActivity)) {
                    activity.startActivity(new Intent(activity, SettingsActivity.class));
                    activity.finish();
                }
                return true;
            }
            return false;
        });
    }
}
