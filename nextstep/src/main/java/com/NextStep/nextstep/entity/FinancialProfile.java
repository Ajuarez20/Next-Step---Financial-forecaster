package com.NextStep.nextstep.entity;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;

@Entity
public class FinancialProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double monthlyIncome;
    private Double monthlyExpenses;
    private Double currentSavings;
    private Double targetGoalAmount;
    private Double debt;

    @OneToMany(mappedBy = "financialProfile", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Expense> expenses = new ArrayList<>();

    @OneToOne(mappedBy = "financialProfile")
    @JsonIgnore
    private UserAccount userAccount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // Standard getter returning null if not provided (prevents overwriting DB data on partial updates)
    public Double getMonthlyIncome() {
        return monthlyIncome;
    }

    @JsonProperty("monthlyIncome")
    public void setMonthlyIncome(Double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    @JsonProperty("income")
    public void setIncomeFromJson(Double income) {
        this.monthlyIncome = income;
    }

    @JsonProperty("monthly_income")
    public void setMonthlyIncomeSnakeCase(Double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Double getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(Double monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }

    // Standard getter returning null if not provided
    public Double getCurrentSavings() {
        return currentSavings;
    }

    @JsonProperty("currentSavings")
    public void setCurrentSavings(Double currentSavings) {
        this.currentSavings = currentSavings;
    }

    @JsonProperty("savings")
    public void setSavingsFromJson(Double savings) {
        this.currentSavings = savings;
    }

    @JsonProperty("current_savings")
    public void setCurrentSavingsSnakeCase(Double currentSavings) {
        this.currentSavings = currentSavings;
    }

    public Double getTargetGoalAmount() {
        return targetGoalAmount;
    }

    public void setTargetGoalAmount(Double targetGoalAmount) {
        this.targetGoalAmount = targetGoalAmount;
    }

    public Double getDebt() {
        return debt;
    }

    public void setDebt(Double debt) {
        this.debt = debt;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
}