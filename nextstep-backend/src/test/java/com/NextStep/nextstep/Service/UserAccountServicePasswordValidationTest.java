package com.NextStep.nextstep.Service;

import com.NextStep.nextstep.entity.UserAccount;
import com.NextStep.nextstep.exception.AuthFailureException;
import com.NextStep.nextstep.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserAccountServicePasswordValidationTest {

    private UserAccountRepository userAccountRepository;
    private PasswordEncoder passwordEncoder;
    private UserAccountService userAccountService;

    @BeforeEach
    void setUp() {
        userAccountRepository = Mockito.mock(UserAccountRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userAccountService = new UserAccountService(userAccountRepository, passwordEncoder);
    }

    @Test
    void registerUser_validPassword_shouldPass() {
        UserAccount user = new UserAccount();
        user.setFirstname("Lock");
        user.setLastname("Tester");
        user.setEmail("valid.user@example.com");
        user.setPassword("ValidPass1!");

        when(passwordEncoder.encode("ValidPass1!")).thenReturn("hashed_pw");
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(inv -> inv.getArgument(0));

        UserAccount saved = userAccountService.registerUser(user);

        assertNotNull(saved);
        assertEquals("hashed_pw", saved.getPassword());
        verify(passwordEncoder, times(1)).encode("ValidPass1!");
        verify(userAccountRepository, times(1)).save(any(UserAccount.class));
    }

    @Test
    void registerUser_passwordTooShort_shouldThrow() {
        UserAccount user = new UserAccount();
        user.setEmail("valid.user@example.com");
        user.setPassword("Aa1!");

        AuthFailureException ex = assertThrows(AuthFailureException.class, () -> userAccountService.registerUser(user));
        assertEquals("Password must be at least 8 characters and include uppercase, lowercase, number, and special character", ex.getMessage());

        verify(userAccountRepository, never()).save(any(UserAccount.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void registerUser_missingUppercase_shouldThrow() {
        UserAccount user = new UserAccount();
        user.setEmail("valid.user@example.com");
        user.setPassword("validpass1!");

        AuthFailureException ex = assertThrows(AuthFailureException.class, () -> userAccountService.registerUser(user));
        assertEquals("Password must be at least 8 characters and include uppercase, lowercase, number, and special character", ex.getMessage());

        verify(userAccountRepository, never()).save(any(UserAccount.class));
    }

    @Test
    void registerUser_missingLowercase_shouldThrow() {
        UserAccount user = new UserAccount();
        user.setEmail("valid.user@example.com");
        user.setPassword("VALIDPASS1!");

        AuthFailureException ex = assertThrows(AuthFailureException.class, () -> userAccountService.registerUser(user));
        assertEquals("Password must be at least 8 characters and include uppercase, lowercase, number, and special character", ex.getMessage());

        verify(userAccountRepository, never()).save(any(UserAccount.class));
    }

    @Test
    void registerUser_missingDigit_shouldThrow() {
        UserAccount user = new UserAccount();
        user.setEmail("valid.user@example.com");
        user.setPassword("ValidPass!!");

        AuthFailureException ex = assertThrows(AuthFailureException.class, () -> userAccountService.registerUser(user));
        assertEquals("Password must be at least 8 characters and include uppercase, lowercase, number, and special character", ex.getMessage());

        verify(userAccountRepository, never()).save(any(UserAccount.class));
    }

    @Test
    void registerUser_missingSpecialCharacter_shouldThrow() {
        UserAccount user = new UserAccount();
        user.setEmail("valid.user@example.com");
        user.setPassword("ValidPass12");

        AuthFailureException ex = assertThrows(AuthFailureException.class, () -> userAccountService.registerUser(user));
        assertEquals("Password must be at least 8 characters and include uppercase, lowercase, number, and special character", ex.getMessage());

        verify(userAccountRepository, never()).save(any(UserAccount.class));
    }
}