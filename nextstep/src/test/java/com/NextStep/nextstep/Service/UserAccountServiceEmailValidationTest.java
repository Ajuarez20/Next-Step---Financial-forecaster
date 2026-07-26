package com.NextStep.nextstep.Service;

import com.NextStep.nextstep.entity.UserAccount;
import com.NextStep.nextstep.exception.AuthFailureException;
import com.NextStep.nextstep.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAccountServiceEmailValidationTest {

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
    void registerUser_validEmail_shouldPassValidation() {
        UserAccount user = new UserAccount();
        user.setFirstname("Lock");
        user.setLastname("Tester");
        user.setEmail("valid.user123@example.com");
        user.setPassword("CorrectPass123!");

        when(passwordEncoder.encode("CorrectPass123!")).thenReturn("hashed_pw");
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserAccount saved = userAccountService.registerUser(user);

        assertNotNull(saved);
        assertEquals("valid.user123@example.com", saved.getEmail());
        assertEquals("hashed_pw", saved.getPassword());
        verify(userAccountRepository, times(1)).save(any(UserAccount.class));
    }

    @Test
    void registerUser_invalidEmail_shouldThrow() {
        UserAccount user = new UserAccount();
        user.setFirstname("Lock");
        user.setLastname("Tester");
        user.setEmail("invalid-email");
        user.setPassword("CorrectPass123!");

        AuthFailureException ex = assertThrows(AuthFailureException.class, () -> userAccountService.registerUser(user));
        assertEquals("Invalid email format", ex.getMessage());

        verify(userAccountRepository, never()).save(any(UserAccount.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void loginUser_invalidEmail_shouldThrowBeforeRepositoryLookup() {
        AuthFailureException ex = assertThrows(
                AuthFailureException.class,
                () -> userAccountService.loginUser("bad-email-format", "whatever")
        );

        assertEquals("Invalid email format", ex.getMessage());
        verify(userAccountRepository, never()).findByEmail(anyString());
    }

    @Test
    void unlockAccount_invalidEmail_shouldThrowBeforeRepositoryLookup() {
        AuthFailureException ex = assertThrows(
                AuthFailureException.class,
                () -> userAccountService.unlockAccount("not-an-email")
        );

        assertEquals("Invalid email format", ex.getMessage());
        verify(userAccountRepository, never()).findByEmail(anyString());
    }
}