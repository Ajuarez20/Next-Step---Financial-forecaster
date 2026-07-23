package com.NextStep.nextstep.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.NextStep.nextstep.entity.Expense;
import com.NextStep.nextstep.entity.FinancialProfile;
import com.NextStep.nextstep.repository.FinancialProfileRepository;


@Service
public class FinancialProfileService {
	private final FinancialProfileRepository financialprofilerepository; 
	private final ExpenseService expenseService;

	public FinancialProfileService (FinancialProfileRepository financialprofilerepository,ExpenseService expenseService) {
		this.financialprofilerepository=financialprofilerepository;
		this.expenseService=expenseService;
		
	}
	// create financial profile
	public  FinancialProfile createFinancialProfile(Double income,Double savings, Double debt) {
		FinancialProfile profile= new FinancialProfile();
		profile.setDebt(debt);
		profile.setIncome(income);
		profile.setSavings(savings);
		
		return financialprofilerepository.save(profile);
	}
	// update financial profile-- used in Controller
	public FinancialProfile updateFinancialProfile(Integer financialProfileId, Double income,Double savings,Double debt) {
		
		FinancialProfile profile=financialprofilerepository.findById(financialProfileId).orElseThrow(() -> new RuntimeException("Financial Profile not found"));
		profile.setIncome(income);
		profile.setDebt( debt);
		profile.setSavings(savings);
		
		return financialprofilerepository.save(profile);
		
	}
		
	// calculates the amount of expenses 
	public Double calculateMonthlyExpenses(Integer financialProfileId) {

	    List<Expense> expenses = 
	            expenseService.getAllExpenses(financialProfileId);

	    Double amount = 0.0;

	    for (Expense expense : expenses) {
	        amount += expense.getAmount();
	    }

	    return amount;
	}
	// calculates the amount of left over income
        public Double calculateRemainingIncome(Integer financialProfileId) {

            FinancialProfile profile =
                    financialprofilerepository.findById(financialProfileId) .orElseThrow(() ->new RuntimeException("Financial Profile not found"));

            Double expenses = calculateMonthlyExpenses(financialProfileId);

            return profile.getIncome() - expenses;
        }
				
	
  
       // calculates how many months of income a users has in savings
	public Double calculateSavingRatio(Integer financialProfileId) {

	    FinancialProfile profile = financialprofilerepository.findById(financialProfileId).orElseThrow(() -> new RuntimeException("Financial Profile not found"));

	   Double savings=profile.getSavings();
	    if ( savings == 0) {
	        return 0.0;
	    }
	    Double income=profile.getIncome();
	    
	    if (income==0)
	    	return 0.0;
	    Double rate = profile.getSavings() / income;

	    return rate;
	}
	
	
	public Double calculateFinancialScore(Integer financialProfileId) {

	    Double score = 0.0;

	    FinancialProfile profile =
	            financialprofilerepository.findById(financialProfileId)
	            .orElseThrow(() -> new RuntimeException("Financial Profile not found"));


	    Double expenses = calculateMonthlyExpenses(financialProfileId);


	    // Income vs Expenses (25 points)

	    if(profile.getIncome() >= 2 * expenses) {

	        score += 25;

	    }
	    else if(profile.getIncome() >= 1.5 * expenses) {

	        score += 20;

	    }
	    else if(profile.getIncome() >= 1.2 * expenses) {
	    	score+=12;
	    }
	    else if(profile.getIncome() >= expenses) {

	        score += 10;

	    }
	    
	    else {
	    	score+=0;
	    }

	// emergency savings aspect of the calculation//
	
	if (profile.getSavings() >= 12* expenses) {
		score+=25; // user have 1 year of expenses saved!!
	}
	else if (profile.getSavings() >= 9* expenses) {
		score+=20;
	}
	else if (profile.getSavings() >= 6* expenses) {
		score+=18;
	}
	else if (profile.getSavings() >= 4* expenses) {
		score+=16;
	}
	else if (profile.getSavings() >= 3* expenses) {
		score+=13;
	}
	else if (profile.getSavings() >= 2* expenses) {
		score+=12;
	}
	else if(profile.getSavings() >= expenses) {
		score+=7;
	}
	else {
		score+=0;
	}
	
	// Next aspect Debt Score calculation
	// ratio is designed based off payoff time using 50% of leftover income
	
	
	// Debt Score
	if (profile.getDebt() == 0.0) {

	    score += 25;
	}
	else {

	    Double monthlyDebtPayment =
	            calculateRemainingIncome(financialProfileId) * 0.5;

	    // User has debt but no money left after expenses
	    if (monthlyDebtPayment > 0) {

	        Double debtRatio =
	                profile.getDebt() / monthlyDebtPayment;

	        if (debtRatio <= 1) {
	            score += 24;
	        }
	        else if (debtRatio <= 3) {
	            score += 23;
	        }
	        else if (debtRatio <= 4) {
	            score += 22;
	        }
	        else if (debtRatio <= 5) {
	            score += 21;
	        }
	        else if (debtRatio <= 6) {
	            score += 19;
	        }
	        else if (debtRatio <= 8) {
	            score += 18;
	        }
	        else if (debtRatio <= 10) {
	            score += 15;
	        }
	        else if (debtRatio <= 12) {
	            score += 14;
	        }
	        else if (debtRatio <= 16) {
	            score += 13;
	        }
	        else if (debtRatio <= 18) {
	            score += 12;
	        }
	        else if (debtRatio <= 24) {
	            score += 10;
	        }
	        else if (debtRatio <= 30) {
	            score += 5;
	        }


	    }

	    // If monthlyDebtPayment <= 0,
	    // the user simply earns 0 points for debt.
	}
	
	// the last part of the basic formula -- score based on financial budgeting percentages
	// Users are penalized if they are outside of the golden rule 50/25/25
	// Needs/Wants/Savings
    Double needs = expenseService.calculateNeedsExpenses(financialProfileId);

    Double wants = expenseService.calculateWantsExpenses(financialProfileId);

    Double remainingIncome = calculateRemainingIncome(financialProfileId);
	
    
    Double needsPercent = (needs / profile.getIncome()) * 100;

    Double wantsPercent = (wants / profile.getIncome()) * 100;

    Double savingsPercent = (remainingIncome / profile.getIncome()) * 100;
    
    if (needsPercent <= 50) {
        score += 10;
    }
    else if (needsPercent <= 55) {
        score += 8;
    }
    else if (needsPercent <= 60) {
        score += 6;
    }
    else if (needsPercent <= 70) {
        score += 3;
    }
    
    if (wantsPercent <= 25) {
        score += 7.5;
    }
    else if (wantsPercent <= 30) {
        score += 6;
    }
    else if (wantsPercent <= 35) {
        score += 4;
    }
    else if (wantsPercent <= 40) {
        score += 2;
    }
    
    if (savingsPercent >= 25) {
        score += 7.5;
    }
    else if (savingsPercent >= 20) {
        score += 6;
    }
    else if (savingsPercent >= 15) {
        score += 4;
    }
    else if (savingsPercent >= 10) {
        score += 2;
    }
    return score;
}// method brace
	
}

	


