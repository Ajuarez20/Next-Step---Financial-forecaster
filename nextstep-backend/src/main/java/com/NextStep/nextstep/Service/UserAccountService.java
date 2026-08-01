package com.NextStep.nextstep.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.NextStep.nextstep.entity.FinancialProfile;
import com.NextStep.nextstep.entity.UserAccount;
import com.NextStep.nextstep.repository.UserAccountRepository;
import java.time.LocalDateTime;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
      
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
        user.setFailedLoginAttempts(0);
        user.setAccountNonLocked(true);
        
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
        user.setFailedLoginAttempts(0);
        user.setAccountNonLocked(true);

        return userAccountRepository.save(user);
    }

    @Transactional
    public UserAccount loginUser(String email, String password) {
        UserAccount user = userAccountRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!user.isAccountNonLocked()) {

            if (user.getLockTime() != null &&
                user.getLockTime().plusMinutes(5).isBefore(LocalDateTime.now())) {

                user.setAccountNonLocked(true);
                user.setFailedLoginAttempts(0);
                user.setLockTime(null);

                userAccountRepository.save(user);

            } else {

                throw new RuntimeException(
                    "Account is temporarily locked. Please try again in 5 minutes."
                );
            }
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            int failedAttempts = user.getFailedLoginAttempts() + 1;

            user.setFailedLoginAttempts(failedAttempts);

            if (failedAttempts >= 5) {
                user.setAccountNonLocked(false);
                user.setLockTime(LocalDateTime.now());
                userAccountRepository.save(user);

                throw new RuntimeException(
                        "Account locked for 5 minutes after 5 failed login attempts."
                );
            }

            userAccountRepository.save(user);

            int attemptsRemaining = 5 - failedAttempts;

            throw new RuntimeException(
                    "Invalid email or password. Attempts remaining: "
                            + attemptsRemaining
            );
        }

        user.setFailedLoginAttempts(0);
        user.setLockTime(null);
        userAccountRepository.save(user);

        return user;
    }

    @Transactional
    public void unlockAccount(String email) {
        UserAccount user = userAccountRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User account not found");
        }

        user.setAccountNonLocked(true);
        user.setFailedLoginAttempts(0);
        user.setLockTime(null);
        userAccountRepository.save(user);
    }
}
