package com.example.savingproject.MODEL;

import com.google.gson.annotations.SerializedName;

public class SavingsSummary {
    @SerializedName("activeGoalCount")
    private int activeGoalCount;
    @SerializedName("totalSaved")
    private double totalSaved;
    @SerializedName("totalTarget")
    private double totalTarget;
    @SerializedName("overdueCount")
    private int overdueCount;

    public int getActiveGoalCount() { return activeGoalCount; }
    public double getTotalSaved() { return totalSaved; }
    public double getTotalTarget() { return totalTarget; }
    public int getOverdueCount() { return overdueCount; }
}
