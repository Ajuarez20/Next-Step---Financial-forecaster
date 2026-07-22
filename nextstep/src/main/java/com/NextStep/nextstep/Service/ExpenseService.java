package com.NextStep.nextstep.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.NextStep.nextstep.entity.Expense;
import com.NextStep.nextstep.entity.FinancialProfile;
import com.NextStep.nextstep.repository.ExpenseRepository;
import com.NextStep.nextstep.repository.FinancialProfileRepository;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final FinancialProfileRepository financialProfileRepository;

    public ExpenseService(ExpenseRepository expenseRepository, FinancialProfileRepository financialProfileRepository) {
        this.expenseRepository = expenseRepository;
        this.financialProfileRepository = financialProfileRepository;
    }

    public Expense addExpense(Integer financialProfileId, Double amount, String category, String description) {
        FinancialProfile profile = financialProfileRepository.findById(financialProfileId)
            .orElseThrow(() -> new RuntimeException("Financial Profile not found"));

        Expense expense = new Expense();
        expense.setAmount(amount);
        expense.setCategory(category);
        expense.setDescription(description);
        expense.setDate(LocalDate.now());

        expense.setFinancialProfile(profile);
        profile.getExpenses().add(expense);

        return expenseRepository.save(expense);
    }

    public Expense getExpenseById(Integer id) {
        return expenseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Expense Not Found"));
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public void deleteExpense(Integer id) {
        expenseRepository.deleteById(id);
    }

    public Expense updateExpense(Integer id, Double amount, String category, String description) {
        Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Expense not Found"));

        expense.setAmount(amount);
        expense.setCategory(category);
        expense.setDescription(description);

        return expenseRepository.save(expense);
    }
}
