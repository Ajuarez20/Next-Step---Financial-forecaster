package com.NextStep.nextstep.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.NextStep.nextstep.entity.FinancialProfile;
import com.NextStep.nextstep.entity.UserAccount;
import com.NextStep.nextstep.exception.AuthFailureException;
import com.NextStep.nextstep.repository.UserAccountRepository;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
public class UserAccountService {

    private static final int MAX_FAILED_ATTEMPTS = 10;
    private static final long LOCKOUT_MINUTES = 60; // 60 minutes

    // Practical email regex (case-insensitive)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
        Pattern.CASE_INSENSITIVE
    );

    // Password regex: at least 8 characters, one uppercase, one lowercase, one number, one special character
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$"
    );

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new AuthFailureException("Password is required");
        }

        String normalized = password.trim();
        if (!PASSWORD_PATTERN.matcher(normalized).matches()) {
            throw new AuthFailureException(
                "Password must be at least 8 characters and include uppercase, lowercase, number, and special character"
            );
        }
    }

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank() || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new AuthFailureException("Invalid email format");
        }
    }

    @Transactional
    public UserAccount registerUser(UserAccount user) {
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());

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

        // Hash exactly once
        user.setPassword(passwordEncoder.encode(user.getPassword().trim()));

        user.setFailedLoginAttempts(0);
        user.setLockedAccount(false);
        user.setLockoutUntil(null);

        return userAccountRepository.save(user);
    }

    @Transactional(noRollbackFor = AuthFailureException.class)
    public UserAccount loginUser(String email, String password) {
        validateEmail(email);

        UserAccount user = userAccountRepository.findByEmail(email);
        if (user == null) throw new AuthFailureException("Invalid email or password");

        LocalDateTime now = LocalDateTime.now();

        if (user.isLockedAccount() && user.getLockoutUntil() != null && now.isBefore(user.getLockoutUntil())) {
            throw new AuthFailureException("Account is locked. Try again later.");
        }

        if (user.getLockoutUntil() != null && !now.isBefore(user.getLockoutUntil())) {
            user.setLockedAccount(false);
            user.setLockoutUntil(null);
            user.setFailedLoginAttempts(0);
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new AuthFailureException("Account password is not set");
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            user.setFailedLoginAttempts(0);
            user.setLockedAccount(false);
            user.setLockoutUntil(null);
            return userAccountRepository.save(user);
        }

        int attempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {
            user.setFailedLoginAttempts(MAX_FAILED_ATTEMPTS);
            user.setLockedAccount(true);
            user.setLockoutUntil(now.plusMinutes(LOCKOUT_MINUTES));
            userAccountRepository.save(user);
            throw new AuthFailureException("Account locked due to too many failed login attempts. Try again in 2 minutes.");
        }

        userAccountRepository.save(user);
        throw new AuthFailureException("Invalid email or password");
    }

    @Transactional
    public UserAccount unlockAccount(String email) {
        validateEmail(email);

        UserAccount user = userAccountRepository.findByEmail(email);
        if (user == null) throw new AuthFailureException("User not found");

        user.setFailedLoginAttempts(0);
        user.setLockedAccount(false);
        user.setLockoutUntil(null);

        return userAccountRepository.save(user);
    }
}