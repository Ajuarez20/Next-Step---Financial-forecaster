package com.NextStep.nextstep.Service;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final int MINIMUM_LENGTH = 8;
    private static final String UPPERCASE_PATTERN = "[A-Z]";
    private static final String SPECIAL_CHAR_PATTERN = "[!@#$%^&*()_+\\-=\\[\\]{};:'\",.<>?/|`~]";

    /**
     * Validates password requirements:
     * - Minimum 8 characters
     * - At least one uppercase letter
     * - At least one special character
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        // Check minimum length
        if (password.length() < MINIMUM_LENGTH) {
            return false;
        }

        // Check for uppercase letter
        if (!Pattern.compile(UPPERCASE_PATTERN).matcher(password).find()) {
            return false;
        }

        // Check for special character
        if (!Pattern.compile(SPECIAL_CHAR_PATTERN).matcher(password).find()) {
            return false;
        }

        return true;
    }

    /**
     * Returns detailed error message for invalid password
     */
    public static String getPasswordErrorMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty";
        }

        if (password.length() < MINIMUM_LENGTH) {
            return "Password must be at least " + MINIMUM_LENGTH + " characters long";
        }

        if (!Pattern.compile(UPPERCASE_PATTERN).matcher(password).find()) {
            return "Password must contain at least one uppercase letter";
        }

        if (!Pattern.compile(SPECIAL_CHAR_PATTERN).matcher(password).find()) {
            return "Password must contain at least one special character (!@#$%^&*()_+-=[]{};:'\",.<>?/|`~)";
        }

        return "Password is valid";
    }
}
