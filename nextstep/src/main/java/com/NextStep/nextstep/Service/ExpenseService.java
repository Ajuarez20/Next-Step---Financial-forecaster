package com.NextStep.nextstep.Service;

import java.time.LocalDate;
import java.util.List;

import com.NextStep.nextstep.entity.Expense;
import com.NextStep.nextstep.entity.FinancialProfile;
import com.NextStep.nextstep.repository.ExpenseRepository;
import com.NextStep.nextstep.repository.FinancialProfileRepository;

import org.springframework.stereotype.Service;

@Service
public class ExpenseService {
	
	private final ExpenseRepository expenserepository;
	private final FinancialProfileRepository financialProfileRepository;
	
	public ExpenseService(ExpenseRepository expenserepository,FinancialProfileRepository financialProfileRepository) {
	this.expenserepository=expenserepository;
	this.financialProfileRepository=financialProfileRepository;
	}

 

// creation of Expense
	public Expense addExpense(Integer FinancialProfileid , Double amount, String category,String description ) {
		FinancialProfile profile=financialProfileRepository.findById(FinancialProfileid).orElseThrow(() -> new RuntimeException ("Financial Profile not found"));
		
		Expense expense= new Expense();
		expense.setAmount(amount);
		
		expense.setCategory(category);
		expense.setDescription(description);
		expense.setDate(LocalDate.now());
		
		expense.setFinancialProfile(profile);
		profile.getExpenses().add(expense);
		
		return expenserepository.save(expense);
			
	}
	public Expense getExpenseById(Integer id) {
		return expenserepository.findById(id).orElseThrow(() -> new RuntimeException("Expense Not Found"));
	}
	public List<Expense> getAllExpenses(Integer financialProfileId){
		 return expenserepository.findByFinancialProfileId(financialProfileId);
	}
	
	public void deleteExpense(Integer id) {
		expenserepository.deleteById(id);
	}
	
	public Expense updateExpense(Integer id, Double amount,String category,String description) {
		
		Expense expense= expenserepository.findById(id).orElseThrow(() -> new RuntimeException("Financial Profile not Found"));
		expense.setAmount(amount);
		expense.setCategory(category);
		expense.setDescription(description);
		
		return expenserepository.save(expense);
		
	}
	
	// Expense Categorization--- NEEDS - required feature
	public Double calculateNeedsExpenses(Integer financialProfileId){

	    List<Expense> expenses = getAllExpenses(financialProfileId);

	    Double total = 0.0;

	    for(Expense expense : expenses){

	        if(expense.getCategory().equals("NEED")){

	            total += expense.getAmount();

	        }
	    }

	    return total;
	}
	// Expense Categorization --- Required Feature --- Wants 
	public Double calculateWantsExpenses(Integer financialProfileId) {
		List<Expense> expenses = getAllExpenses(financialProfileId);
		 Double total = 0.0;
		 
		 for (Expense expense: expenses){
			 if(expense.getCategory().equals("Want")) {
				 total +=expense.getAmount();
			 }
		 }
		 return total;
		
		
	}
	}


