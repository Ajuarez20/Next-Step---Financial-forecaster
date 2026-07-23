package com.NextStep.nextstep.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NextStep.nextstep.Service.ExpenseService;
import com.NextStep.nextstep.entity.Expense;




@RestController
@RequestMapping("/expense")
public class ExpenseController {
	private final ExpenseService expenseservice;

	public ExpenseController(ExpenseService expenseservice) {
		this.expenseservice=expenseservice;
	}
@PostMapping("/{financialProfileId}/expenses")
public Expense addExpense(@PathVariable Integer financialProfileId, @RequestBody Expense expense) {
	return expenseservice.addExpense(financialProfileId,expense.getAmount(),expense.getCategory(),expense.getDescription());
}

@GetMapping("/financialprofiles/{financialProfileId}/expenses")
public List<Expense> getAllExpenses(@PathVariable Integer financialProfileId){
	return expenseservice.getAllExpenses(financialProfileId);	
}

@PutMapping("/expenses/{financialProfileId}")
public Expense updateExpense(@PathVariable Integer id, @RequestBody Expense expense) {
	return expenseservice.updateExpense(id,expense.getAmount(),expense.getCategory(),expense.getDescription());
}

@DeleteMapping("{id}")
public void deleteExpense(@PathVariable Integer id) {
 expenseservice.deleteExpense(id);
}



}








