package com.NextStep.nextstep.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.NextStep.nextstep.entity.FinancialProfile;
import com.NextStep.nextstep.entity.UserAccount;
import com.NextStep.nextstep.repository.UserAccountRepository;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional
    public UserAccount registerUser(UserAccount user) {
        System.out.println("New User start here: Enter Information to begin");

        // Automatically attach a blank profile if one isn't attached yet
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

        // Hash the password before saving (if present)
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String hashed = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashed);
        }

        return userAccountRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserAccount loginUser(String email, String password) {
        UserAccount user = userAccountRepository.findByEmail(email);

        if (user != null && user.getPassword() != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        throw new RuntimeException("Invalid email or password");
    }
}
