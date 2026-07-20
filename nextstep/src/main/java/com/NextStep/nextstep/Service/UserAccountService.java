package com.NextStep.nextstep.Service;

import org.springframework.stereotype.Service;

import com.NextStep.nextstep.entity.FinancialProfile;
import com.NextStep.nextstep.entity.UserAccount;
import com.NextStep.nextstep.repository.UserAccountRepository;

@Service
public class UserAccountService {
	  private final UserAccountRepository userAccountRepository;
	  
	  public UserAccountService(UserAccountRepository userAccountRepository) {
		    this.userAccountRepository = userAccountRepository;
		}  
	  
public UserAccount registerUser( String firstname,String lastname,String email,String password) {
	
	UserAccount user= new UserAccount();
	FinancialProfile profile= new FinancialProfile();
	
	user.setFirstname(firstname);
	user.setLastname(lastname);
	user.setEmail(email);
	user.setPassword(password);
	
	profile.setIncome(0.0);
	profile.setSavings(0.0);
	profile.setDebt(0.0);
	
	user.setFinancialProfile(profile);
	profile.setUserAccount(user);
	
	
	return userAccountRepository.save(user);
}
	
}

