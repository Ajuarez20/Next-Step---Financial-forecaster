package com.NextStep.nextstep.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NextStep.nextstep.Service.FinancialProfileService;
import com.NextStep.nextstep.entity.FinancialProfile;

@RestController
@RequestMapping("/financialprofile")
public class FinancialProfileController {
	private final FinancialProfileService financialProfileService;
	
	public FinancialProfileController(FinancialProfileService financialProfileService) {
	this.financialProfileService=financialProfileService;	
}
@PutMapping ("/{id}") // updating the financialProfile through the frontend
public FinancialProfile UpdateFiancialProfile(@PathVariable Integer id, @RequestBody FinancialProfile profile) {
	return financialProfileService.updateFinancialProfile(id,profile.getIncome(),profile.getSavings(),profile.getDebt());
}
// monthly expenses
 @GetMapping("/{id}/monthlyexpenses")
 public Double calculateMonthlyExpenses(@PathVariable Integer id) {
 return financialProfileService.calculateMonthlyExpenses(id);
 
 }
 // remaining income
 @GetMapping("/{id}/remaniningincome")
 public Double calculateRemainingIncome(@PathVariable Integer id) {
	 return financialProfileService.calculateRemainingIncome(id);
 }
 // savings ratio-- amount of monthly income a user has saved up
 @GetMapping("/{id}/savingratio")
 public Double calculateSavingsRatio(@PathVariable Integer id) {
	 return financialProfileService.calculateSavingRatio(id);
 }
 // 
 @GetMapping("/{id}/financialscore")
 public Double calculateFinancialScore(@PathVariable Integer id) {
	 return financialProfileService.calculateFinancialScore(id);
}
}
