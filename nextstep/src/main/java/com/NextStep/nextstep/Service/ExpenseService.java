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
	public List<Expense> getAllExpenses(){
		return expenserepository.findAll();
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
	}


