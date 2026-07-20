package com.NextStep.nextstep.Service;

import java.util.ArrayList;


import org.springframework.stereotype.Service;

import com.NextStep.nextstep.entity.Expense;
import com.NextStep.nextstep.entity.FinancialProfile;
import com.NextStep.nextstep.repository.FinancialProfileRepository;


@Service
public class FinancialProfileService {
	private final FinancialProfileRepository financialprofilerepository; 


	public FinancialProfileService (FinancialProfileRepository financialprofilerepository) {
		this.financialprofilerepository=financialprofilerepository;
	}
	public  FinancialProfile createFinancialProfile(Double income,Double savings, Double debt) {
		FinancialProfile profile= new FinancialProfile();
		profile.setDebt(debt);
		profile.setIncome(income);
		profile.setSavings(savings);
		
		return profile;
		
	}
	public FinancialProfile calculateMonthlyExpenses() {
	}
		
		
		
	}
	


