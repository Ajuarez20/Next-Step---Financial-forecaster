package com.NextStep.nextstep.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.NextStep.nextstep.entity.FinancialProfile;
import com.NextStep.nextstep.entity.UserAccount;
import com.NextStep.nextstep.repository.UserAccountRepository;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
      
    // The constructor now takes BOTH the repository and the encoder
    public UserAccountService(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }  
      
    @Transactional
    public UserAccount registerUser(String firstname, String lastname, String email, String password) {
        UserAccount user = new UserAccount();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        
        // Encode the password before saving it to the database
        user.setPassword(passwordEncoder.encode(password));
        
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
        // Encode the password for this registration method too
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

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

        // Securely compare the raw password against the encoded password in the database
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        throw new RuntimeException("Invalid email or password");
    }

    @Transactional
    public void unlockAccount(String email) {
        UserAccount user = userAccountRepository.findByEmail(email);
        if (user != null) {
            user.setAccountNonLocked(true);
            userAccountRepository.save(user);
        }
    }
}
