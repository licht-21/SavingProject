package com.example.savingproject.MODEL;

public class SavingsGoal {
    private int id;
    private String name;
    private double targetAmount;
    private double currentAmount;

    public SavingsGoal(int id, String name, double targetAmount, double currentAmount) {
        this.id = id;
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getTargetAmount() { return targetAmount; }
    public double getCurrentAmount() { return currentAmount; }
}
