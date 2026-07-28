package com.NextStep.nextstep.Service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.NextStep.nextstep.entity.Expense;
import com.NextStep.nextstep.entity.FinancialProfile;
import com.NextStep.nextstep.repository.FinancialProfileRepository;

@Service
public class FinancialProfileService {

    private final FinancialProfileRepository financialProfileRepository;
    private final ExpenseService expenseService;

    public FinancialProfileService(FinancialProfileRepository financialProfileRepository, ExpenseService expenseService) {
        this.financialProfileRepository = financialProfileRepository;
        this.expenseService = expenseService;
    }

    @Transactional
    public FinancialProfile createFinancialProfile(Double monthlyIncome, Double currentSavings, Double debt) {
        FinancialProfile profile = new FinancialProfile();
        profile.setMonthlyIncome(monthlyIncome != null ? monthlyIncome : 0.0);
        profile.setCurrentSavings(currentSavings != null ? currentSavings : 0.0);
        profile.setDebt(debt != null ? debt : 0.0);
        
        return financialProfileRepository.save(profile);
    }

    @Transactional
    public FinancialProfile updateFinancialProfile(
            Integer financialProfileId, 
            Double monthlyIncome, 
            Double currentSavings, 
            Double debt, 
            Double targetGoalAmount, 
            Double monthlyExpenses) {
        
        // Automatically creates the profile if it doesn't exist yet in the database
        FinancialProfile profile = financialProfileRepository.findById(financialProfileId)
            .orElseGet(() -> {
                FinancialProfile newProfile = new FinancialProfile();
                newProfile.setId(financialProfileId);
                return newProfile;
            });
        
        if (monthlyIncome != null) profile.setMonthlyIncome(monthlyIncome);
        if (currentSavings != null) profile.setCurrentSavings(currentSavings);
        if (debt != null) profile.setDebt(debt);
        if (targetGoalAmount != null) profile.setTargetGoalAmount(targetGoalAmount);
        if (monthlyExpenses != null) profile.setMonthlyExpenses(monthlyExpenses);
        
        return financialProfileRepository.save(profile);
    }

    public FinancialProfile getProfileById(Integer profileId) {
        return financialProfileRepository.findById(profileId)
            .orElseThrow(() -> new RuntimeException("Financial Profile not found"));
    }
        
    @Transactional
    public Double calculateMonthlyExpenses(Integer financialProfileId) {
        FinancialProfile profile = getProfileById(financialProfileId);
        List<Expense> expenses = expenseService.getAllExpenses(financialProfileId);

        Double totalExpenses = 0.0;
        if (expenses != null) {
            for (Expense expense : expenses) {
                if (expense.getAmount() != null) {
                    totalExpenses += expense.getAmount();
                }
            }
        }

        profile.setMonthlyExpenses(totalExpenses);
        financialProfileRepository.save(profile);
        
        return totalExpenses;
    }

    public Double calculateRemainingIncome(Integer financialProfileId) {
        FinancialProfile profile = getProfileById(financialProfileId);
        Double expenses = calculateMonthlyExpenses(financialProfileId);
        Double income = profile.getMonthlyIncome() != null ? profile.getMonthlyIncome() : 0.0;

        return income - expenses;
    }
                
    public Double calculateSavingRatio(Integer financialProfileId) {
        FinancialProfile profile = getProfileById(financialProfileId);

        Double savings = profile.getCurrentSavings() != null ? profile.getCurrentSavings() : 0.0;
        if (savings == 0.0) return 0.0;
        
        Double income = profile.getMonthlyIncome() != null ? profile.getMonthlyIncome() : 0.0;
        if (income == 0.0) return 0.0;

        return savings / income;
    }
    
    public Double calculateFinancialScore(Integer financialProfileId) {
        return calculateProjectedFinancialScore(financialProfileId, null, 0.0);
    }

    public Double calculateProjectedFinancialScore(Integer financialProfileId, Double projectedSavings) {
        return calculateProjectedFinancialScore(financialProfileId, projectedSavings, 0.0);
    }

    public Double calculateProjectedFinancialScore(Integer financialProfileId, Double projectedSavings, Double newMonthlyPayment) {
        FinancialProfile profile = getProfileById(financialProfileId);

        Double expenses = calculateMonthlyExpenses(financialProfileId);
        Double income = profile.getMonthlyIncome() != null ? profile.getMonthlyIncome() : 0.0;
        Double effectiveSavings = (projectedSavings != null) ? projectedSavings : (profile.getCurrentSavings() != null ? profile.getCurrentSavings() : 0.0);
        Double debt = profile.getDebt() != null ? profile.getDebt() : 0.0;

        if (income <= 0.0) {
            return 0.0;
        }

        Double score = 0.0;

        // 1. Income vs Expenses (25 points)
        if (income >= 2 * expenses) score += 25;
        else if (income >= 1.5 * expenses) score += 20;
        else if (income >= 1.2 * expenses) score += 12;
        else if (income >= expenses) score += 10;
        else score += 5;

        // 2. Emergency Savings (25 points)
        if (expenses > 0) {
            if (effectiveSavings >= 12 * expenses) score += 25;
            else if (effectiveSavings >= 9 * expenses) score += 20;
            else if (effectiveSavings >= 6 * expenses) score += 18;
            else if (effectiveSavings >= 4 * expenses) score += 16;
            else if (effectiveSavings >= 3 * expenses) score += 13;
            else if (effectiveSavings >= 2 * expenses) score += 12;
            else if (effectiveSavings >= expenses) score += 7;
            else if (effectiveSavings > 0) score += 4;
        } else if (effectiveSavings > 0) {
            score += 25;
        }

        // 3. Debt Score (25 points)
        if (debt == 0.0) {
            score += 25;
        } else {
            Double baseRemaining = calculateRemainingIncome(financialProfileId);
            Double monthlyDebtPayment = (baseRemaining - (newMonthlyPayment != null ? newMonthlyPayment : 0.0)) * 0.5;
            if (monthlyDebtPayment <= 0) monthlyDebtPayment = 1.0;

            Double debtRatio = debt / monthlyDebtPayment;
            if (debtRatio <= 1) score += 24;
            else if (debtRatio <= 3) score += 23;
            else if (debtRatio <= 5) score += 21;
            else if (debtRatio <= 10) score += 15;
            else if (debtRatio <= 20) score += 10;
            else score += 5;
        }

        // 4. Budgeting Percentages - 50/25/25 Rule (25 points)
        Double needs = expenseService.calculateNeedsExpenses(financialProfileId);
        Double wants = expenseService.calculateWantsExpenses(financialProfileId) + (newMonthlyPayment != null ? newMonthlyPayment : 0.0);
        Double projectedRemainingIncome = calculateRemainingIncome(financialProfileId) - (newMonthlyPayment != null ? newMonthlyPayment : 0.0);
        
        Double needsPercent = (needs / income) * 100;
        Double wantsPercent = (wants / income) * 100;
        Double savingsPercent = (projectedRemainingIncome / income) * 100;
        
        if (needsPercent <= 50) score += 10;
        else if (needsPercent <= 60) score += 6;
        else score += 2;
        
        if (wantsPercent <= 25) score += 7.5;
        else if (wantsPercent <= 35) score += 4;
        else score += 1;
        
        if (savingsPercent >= 25) score += 7.5;
        else if (savingsPercent >= 15) score += 4;
        else if (savingsPercent > 0) score += 1;

        return Math.min(100.0, Math.max(0.0, score));
    }
}