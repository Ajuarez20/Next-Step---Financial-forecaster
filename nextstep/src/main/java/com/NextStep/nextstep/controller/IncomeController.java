package com.NextStep.nextstep.controller;

import com.NextStep.nextstep.entity.Income;
import com.NextStep.nextstep.entity.FinancialProfile;
import com.NextStep.nextstep.repository.IncomeRepository;
import com.NextStep.nextstep.repository.FinancialProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/income")
public class IncomeController {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private FinancialProfileRepository profileRepository;

    @GetMapping("/all")
    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    @PostMapping("/add/{profileId}")
    public ResponseEntity<?> addIncome(@PathVariable Integer profileId, @RequestBody Income income) {
        try {
            FinancialProfile profile = profileRepository.findById(profileId)
                    .orElseGet(() -> {
                        FinancialProfile newProfile = new FinancialProfile();
                        return profileRepository.save(newProfile);
                    });

            income.setFinancialProfile(profile);
            if (income.getDate() == null) {
                income.setDate(LocalDate.now());
            }

            Income savedIncome = incomeRepository.save(income);
            return ResponseEntity.ok(savedIncome);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to save income: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIncome(@PathVariable Integer id) {
        incomeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}