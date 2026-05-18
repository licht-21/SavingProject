package com.example.savingproject.DATA;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREFS_NAME = "saving_session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";

    private static SessionManager instance;
    private final SharedPreferences prefs;

    private SessionManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
    }

    public static SessionManager getInstance() {
        return instance;
    }

    public void saveSession(int userId, String email, String username) {
        prefs.edit()
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_EMAIL, email)
                .putString(KEY_USERNAME, username)
                .apply();
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    public boolean isLoggedIn() {
        return getUserId() > 0;
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
}
