package com.example.savingproject;

public class SavingsGoal {
    private Integer id; // Use Integer so it can be null for new goals
    private String name;
    private double targetAmount;
    private double currentAmount;
    private String targetDate; // Simple string for now (e.g., "2023-12-31")

    public SavingsGoal(Integer id, String name, double targetAmount, double currentAmount, String targetDate) {
        this.id = id;
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.targetDate = targetDate;
    }

    // Constructor for creating new goals (no ID yet)
    public SavingsGoal(String name, double targetAmount, double currentAmount, String targetDate) {
        this.id = null;
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.targetDate = targetDate;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(double targetAmount) { this.targetAmount = targetAmount; }
    
    public double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(double currentAmount) { this.currentAmount = currentAmount; }
    
    public String getTargetDate() { return targetDate; }
    public void setTargetDate(String targetDate) { this.targetDate = targetDate; }
}
