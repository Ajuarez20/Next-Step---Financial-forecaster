package com.NextStep.nextstep.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.NextStep.nextstep.Service.ExpenseService;
import com.NextStep.nextstep.entity.Expense;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // Global fetch for all expenses (used by your frontend dashboard)
    @GetMapping("/all")
    public List<Expense> getAllExpensesGlobal() {
        return expenseService.getAllExpenses();
    }

    // Frontend compatible add route
    @PostMapping("/add/{financialProfileId}")
    public ResponseEntity<?> addExpenseFrontend(
            @PathVariable Integer financialProfileId, 
            @RequestBody Expense expense) {
        try {
            Expense savedExpense = expenseService.addExpense(
                financialProfileId, 
                expense.getAmount(), 
                expense.getCategory(), 
                expense.getDescription()
            );
            return ResponseEntity.ok(savedExpense);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to add expense: " + e.getMessage());
        }
    }

    // Teammate's add expense route pattern
    @PostMapping("/{financialProfileId}/expenses")
    public ResponseEntity<?> addExpenseTeammate(
            @PathVariable Integer financialProfileId, 
            @RequestBody Expense expense) {
        try {
            Expense savedExpense = expenseService.addExpense(
                financialProfileId, 
                expense.getAmount(), 
                expense.getCategory(), 
                expense.getDescription()
            );
            return ResponseEntity.ok(savedExpense);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to add expense: " + e.getMessage());
        }
    }

    // Teammate's get expenses by financial profile pattern
    @GetMapping("/financialprofiles/{financialProfileId}/expenses")
    public List<Expense> getExpensesByFinancialProfile(@PathVariable Integer financialProfileId) {
        return expenseService.getAllExpenses(financialProfileId);
    }

    // Update expense by ID (Fixed path variable and parameter mismatch bug)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(
            @PathVariable Integer id, 
            @RequestBody Expense expense) {
        try {
            Expense updated = expenseService.updateExpense(
                id, 
                expense.getAmount(), 
                expense.getCategory(), 
                expense.getDescription()
            );
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to update expense: " + e.getMessage());
        }
    }

    // Delete expense by ID (Fixed missing leading slash bug)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Integer id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok().build();
    }
}