package com.NextStep.nextstep.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Expense {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Double amount;
	private String category;
	private String description;
	
	@ManyToOne
	private FinancialProfile financialProfile; 
	@JoinColumn(name="finanical_profile_id")
	// relationship between Financial profile and expenses
	
	private LocalDate date;
	public int getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public FinancialProfile getFinancialProfile() {
		return financialProfile;
	}
	public void setFinancialProfile(FinancialProfile financialProfile) {
		this.financialProfile = financialProfile;
	}
	
	

}
