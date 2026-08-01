package com.NextStep.nextstep.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.NextStep.nextstep.Service.FinancialProfileService;
import com.NextStep.nextstep.entity.FinancialProfile;

@RestController
@RequestMapping("/api/financialprofile")
@CrossOrigin(origins = "http://localhost:5174", methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST, RequestMethod.OPTIONS})
public class FinancialProfileController {

    private final FinancialProfileService financialProfileService;
    
    public FinancialProfileController(FinancialProfileService financialProfileService) {
        this.financialProfileService = financialProfileService;   
    }

    @PutMapping("/{id}")
    public FinancialProfile updateFinancialProfile(@PathVariable Integer id, @RequestBody FinancialProfile profile) {
        return financialProfileService.updateFinancialProfile(
            id, 
            profile.getMonthlyIncome(),    
            profile.getCurrentSavings(),   
            profile.getDebt(),
            profile.getTargetGoalAmount(),
            profile.getMonthlyExpenses()
        );
    }

    @GetMapping("/{id}")
    public FinancialProfile getFinancialProfile(@PathVariable Integer id) {
        return financialProfileService.getProfileById(id);
    }

    @GetMapping("/{id}/monthlyexpenses")
    public Double calculateMonthlyExpenses(@PathVariable Integer id) {
        return financialProfileService.calculateMonthlyExpenses(id);
    }

    @GetMapping("/{id}/remainingincome")
    public Double calculateRemainingIncome(@PathVariable Integer id) {
        return financialProfileService.calculateRemainingIncome(id);
    }

    @GetMapping("/{id}/savingratio")
    public Double calculateSavingsRatio(@PathVariable Integer id) {
        return financialProfileService.calculateSavingRatio(id);
    }

    @GetMapping("/{id}/financialscore")
    public Double calculateFinancialScore(@PathVariable Integer id) {
        return financialProfileService.calculateFinancialScore(id);
    }

    @GetMapping("/{id}/projectedscore")
    public Double calculateProjectedFinancialScore(
            @PathVariable Integer id, 
            @RequestParam Double projectedSavings) {
        return financialProfileService.calculateProjectedFinancialScore(id, projectedSavings);
    }
}