package com.NextStep.nextstep.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "financial_profile")
public class FinancialProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double monthlyIncome;
    private Double monthlyExpenses;
    private Double currentSavings;
    private Double targetGoalAmount;

    public FinancialProfile() {}

    public FinancialProfile(Double monthlyIncome, Double monthlyExpenses, Double currentSavings, Double targetGoalAmount) {
        this.monthlyIncome = monthlyIncome;
        this.monthlyExpenses = monthlyExpenses;
        this.currentSavings = currentSavings;
        this.targetGoalAmount = targetGoalAmount;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public Double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(Double monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public Double getMonthlyExpenses() { return monthlyExpenses; }
    public void setMonthlyExpenses(Double monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }

    public Double getCurrentSavings() { return currentSavings; }
    public void setCurrentSavings(Double currentSavings) { this.currentSavings = currentSavings; }

    public Double getTargetGoalAmount() { return targetGoalAmount; }
    public void setTargetGoalAmount(Double targetGoalAmount) { this.targetGoalAmount = targetGoalAmount; }
}