package com.NextStep.nextstep.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.NextStep.nextstep.Service.UserAccountService;
import com.NextStep.nextstep.entity.UserAccount;

@RestController
@CrossOrigin(origins = "*")
public class UserAccountController {
    
    private final UserAccountService userAccountService;
    
    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserAccount user) {
        try {
            UserAccount registeredUser = userAccountService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse("Registration failed: " + e.getMessage())
            );
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserAccount user) {
        try {
            UserAccount loggedInUser = userAccountService.loginUser(user.getEmail(), user.getPassword());
            return ResponseEntity.ok(loggedInUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorResponse(e.getMessage())
            );
        }
    }

    @PostMapping("/unlock/{email}")
    public ResponseEntity<?> unlockAccount(@PathVariable String email) {
        try {
            userAccountService.unlockAccount(email);
            return ResponseEntity.ok(new SuccessResponse("Account unlocked successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse("Failed to unlock account: " + e.getMessage())
            );
        }
    }

    // Inner classes for API responses
    public static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) {
            this.error = error;
        }
        public String getError() {
            return error;
        }
    }

    public static class SuccessResponse {
        public String message;
        public SuccessResponse(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
