package com.NextStep.nextstep.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.NextStep.nextstep.entity.Expense;
import com.NextStep.nextstep.entity.FinancialProfile;
import com.NextStep.nextstep.repository.FinancialProfileRepository;

@Service
public class FinancialProfileService {

    private final FinancialProfileRepository financialProfileRepository;

    public FinancialProfileService(FinancialProfileRepository financialProfileRepository) {
        this.financialProfileRepository = financialProfileRepository;
    }

    public FinancialProfile createFinancialProfile(Double income, Double savings, Double debt) {
        FinancialProfile profile = new FinancialProfile();
        profile.setMonthlyIncome(income);
        profile.setCurrentSavings(savings);
        profile.setDebt(debt);
        return financialProfileRepository.save(profile);
    }

    public FinancialProfile getProfileById(Integer profileId) {
        return financialProfileRepository.findById(profileId)
            .orElseThrow(() -> new RuntimeException("Financial Profile not found"));
    }

    public Double calculateMonthlyExpenses(Integer profileId) {
        FinancialProfile profile = getProfileById(profileId);

        double totalExpenses = 0.0;
        if (profile.getExpenses() != null) {
            for (Expense expense : profile.getExpenses()) {
                if (expense.getAmount() != null) {
                    totalExpenses += expense.getAmount();
                }
            }
        }

        profile.setMonthlyExpenses(totalExpenses);
        financialProfileRepository.save(profile);
        return totalExpenses;
    }

    public Map<String, Object> calculateFinancialScore(Integer profileId) {
        FinancialProfile profile = getProfileById(profileId);

        double income = profile.getMonthlyIncome() != null ? profile.getMonthlyIncome() : 0.0;
        double expenses = profile.getMonthlyExpenses() != null ? profile.getMonthlyExpenses() : 0.0;
        double savings = profile.getCurrentSavings() != null ? profile.getCurrentSavings() : 0.0;

        int score = 0;
        if (income > 0) score += 35;
        if (expenses < income) score += 35;
        if (savings > 0) score += 30;

        String rating = score >= 80 ? "Excellent" : score >= 50 ? "Good" : "Needs Attention";

        Map<String, Object> response = new HashMap<>();
        response.put("score", score);
        response.put("rating", rating);
        response.put("income", income);
        response.put("expenses", expenses);
        response.put("savings", savings);

        return response;
    }
}