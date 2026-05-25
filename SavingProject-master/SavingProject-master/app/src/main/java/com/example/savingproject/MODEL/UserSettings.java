package com.example.savingproject.MODEL;

import com.google.gson.annotations.SerializedName;

public class UserSettings {
    @SerializedName("darkMode")
    private boolean darkMode;
    @SerializedName("dateFormat")
    private String dateFormat;

    public boolean isDarkMode() { return darkMode; }
    public String getDateFormat() { return dateFormat; }
}
