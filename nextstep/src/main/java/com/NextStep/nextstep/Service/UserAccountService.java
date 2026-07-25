package com.NextStep.nextstep.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.NextStep.nextstep.entity.FinancialProfile;
import com.NextStep.nextstep.entity.UserAccount;
import com.NextStep.nextstep.repository.UserAccountRepository;

import java.time.LocalDateTime;

@Service
public class UserAccountService {

    private static final int MAX_FAILED_ATTEMPTS = 10;
    private static final long LOCKOUT_MINUTES = 2; // testing (change to 60 for production)

    private final UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional
    public UserAccount registerUser(UserAccount user) {
        System.out.println("New User start here: Enter Information to begin");

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
        user.setLockoutUntil(null);

        return userAccountRepository.save(user);
    }

    @Transactional
    public UserAccount loginUser(String email, String password) {
        UserAccount user = userAccountRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("Invalid email or password");
        }

        LocalDateTime now = LocalDateTime.now();

        if (user.getLockoutUntil() != null && now.isBefore(user.getLockoutUntil())) {
            throw new RuntimeException("Account is locked. Try again later.");
        }

        if (user.getLockoutUntil() != null && !now.isBefore(user.getLockoutUntil())) {
            user.setLockoutUntil(null);
            user.setFailedLoginAttempts(0);
        }

        if (user.getPassword().equals(password)) {
            user.setFailedLoginAttempts(0);
            user.setLockoutUntil(null);
            return userAccountRepository.save(user);
        }

        int attempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {
            user.setLockoutUntil(now.plusMinutes(LOCKOUT_MINUTES));
            userAccountRepository.save(user);
            throw new RuntimeException("Account locked due to too many failed login attempts. Try again in 2 minutes.");
        }

        userAccountRepository.save(user);
        throw new RuntimeException("Invalid email or password");
    }
}