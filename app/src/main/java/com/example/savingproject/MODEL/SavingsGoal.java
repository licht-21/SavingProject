package com.example.savingproject.MODEL;

import com.google.gson.annotations.SerializedName;

public class SavingsGoal {
    private int id;
    private String name;
    @SerializedName("targetAmount")
    private double targetAmount;
    @SerializedName("currentAmount")
    private double currentAmount;
    @SerializedName("progressPercent")
    private int progressPercent;
    @SerializedName("dueDate")
    private String dueDate;
    @SerializedName("isArchived")
    private boolean isArchived;
    @SerializedName("archivedAt")
    private String archivedAt;
    @SerializedName("isOverdue")
    private boolean isOverdue;

    public int getId() { return id; }
    public String getName() { return name; }
    public double getTargetAmount() { return targetAmount; }
    public double getCurrentAmount() { return currentAmount; }
    public int getProgressPercent() { return progressPercent; }
    public String getDueDate() { return dueDate; }
    public boolean isArchived() { return isArchived; }
    public String getArchivedAt() { return archivedAt; }
    public boolean isOverdue() { return isOverdue; }

    public int computeProgressPercent() {
        if (isArchived) {
            return 100;
        }
        if (targetAmount <= 0) {
            return 0;
        }
        if (progressPercent > 0) {
            return Math.min(100, progressPercent);
        }
        return Math.min(100, (int) ((currentAmount / targetAmount) * 100));
    }
}
