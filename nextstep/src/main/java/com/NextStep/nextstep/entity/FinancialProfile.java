package com.NextStep.nextstep.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class FinancialProfile {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Double income;
	private Double savings;
	private Double debt;
	

	@OneToMany(mappedBy = "financialProfile")
	// Relationship between financialProfile/expenses
	private List<Expense> expenses=new ArrayList<>();
	
	
	
	@OneToOne(mappedBy="financialProfile")
	@JsonIgnore
	private UserAccount userAccount;
	
	public Integer getId() {
		return id;
	}
	 public void setId(Integer id) {
		 this.id = id;
	 }
	
	public List<Expense> getExpenses() {
		return expenses;
	}
	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}
	public double getIncome() {
		return income;
	}
	public void setIncome(double income) {
		this.income = income;
	}
	public double getSavings() {
		return savings;
	}
	public void setSavings(double savings) {
		this.savings = savings;
	}
	public double getDebt() {
		return debt;
	}
	public void setDebt(double debt) {
		this.debt = debt;
	}
	
	public UserAccount getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}
	

	
}
