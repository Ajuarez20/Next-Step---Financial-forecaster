package com.NextStep.nextstep.Service;

public class ForecastRequest {
    private double monthlyIncome;
    private double monthlyExpenses;
    private double currentSavings;
    private double targetGoalAmount;
    private int projectionMonths;

    public ForecastRequest() {}

    public double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(double monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public double getMonthlyExpenses() { return monthlyExpenses; }
    public void setMonthlyExpenses(double monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }

    public double getCurrentSavings() { return currentSavings; }
    public void setCurrentSavings(double currentSavings) { this.currentSavings = currentSavings; }

    public double getTargetGoalAmount() { return targetGoalAmount; }
    public void setTargetGoalAmount(double targetGoalAmount) { this.targetGoalAmount = targetGoalAmount; }

    public int getProjectionMonths() { return projectionMonths; }
    public void setProjectionMonths(int projectionMonths) { this.projectionMonths = projectionMonths; }
}