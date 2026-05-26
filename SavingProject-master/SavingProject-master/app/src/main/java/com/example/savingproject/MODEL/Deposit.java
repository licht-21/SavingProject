package com.example.savingproject.MODEL;

import com.google.gson.annotations.SerializedName;

public class Deposit {
    private int id;
    @SerializedName("goalId")
    private int goalId;
    private double amount;
    private String note;
    @SerializedName("createdAt")
    private String createdAt;

    public int getId() { return id; }
    public int getGoalId() { return goalId; }
    public double getAmount() { return amount; }
    public String getNote() { return note; }
    public String getCreatedAt() { return createdAt; }
}
