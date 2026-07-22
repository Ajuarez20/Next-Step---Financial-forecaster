package com.NextStep.nextstep.controller;

import com.NextStep.nextstep.entity.Expense;
import com.NextStep.nextstep.entity.FinancialProfile;
import com.NextStep.nextstep.repository.ExpenseRepository;
import com.NextStep.nextstep.repository.FinancialProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private FinancialProfileRepository profileRepository;

    @GetMapping("/all")
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @PostMapping("/add/{profileId}")
    public ResponseEntity<?> addExpense(@PathVariable Integer profileId, @RequestBody Expense expense) {
        try {
            FinancialProfile profile = profileRepository.findById(profileId)
                    .orElseGet(() -> {
                        FinancialProfile newProfile = new FinancialProfile();
                        return profileRepository.save(newProfile);
                    });

            expense.setFinancialProfile(profile);
            if (expense.getDate() == null) {
                expense.setDate(LocalDate.now());
            }

            Expense savedExpense = expenseRepository.save(expense);
            return ResponseEntity.ok(savedExpense);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to save expense: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Integer id) {
        expenseRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
