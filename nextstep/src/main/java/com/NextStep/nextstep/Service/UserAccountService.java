package com.NextStep.nextstep.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.NextStep.nextstep.entity.FinancialProfile;
import com.NextStep.nextstep.entity.UserAccount;
import com.NextStep.nextstep.repository.UserAccountRepository;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
      
    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }  
      
    @Transactional
    public UserAccount registerUser(String firstname, String lastname, String email, String password) {
        UserAccount user = new UserAccount();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPassword(password);
        
        FinancialProfile profile = new FinancialProfile();
        profile.setMonthlyIncome(0.0);
        profile.setMonthlyExpenses(0.0);
        profile.setCurrentSavings(0.0);
        profile.setTargetGoalAmount(0.0);
        profile.setDebt(0.0);
        
        user.setFinancialProfile(profile);
        profile.setUserAccount(user);
        
        return userAccountRepository.save(user);
    }

    @Transactional
    public UserAccount registerUser(UserAccount user) {
        if (user.getFinancialProfile() == null) {
            FinancialProfile profile = new FinancialProfile();
            profile.setMonthlyIncome(0.0);
            profile.setMonthlyExpenses(0.0);
            profile.setCurrentSavings(0.0);
            profile.setTargetGoalAmount(0.0);
            profile.setDebt(0.0);

            user.setFinancialProfile(profile);
            profile.setUserAccount(user);
        }

        return userAccountRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserAccount loginUser(String email, String password) {
        UserAccount user = userAccountRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        throw new RuntimeException("Invalid email or password");
    }
}