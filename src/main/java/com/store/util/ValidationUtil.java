package com.store.util;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    
    private static final String PHONE_REGEX = "^[+]?[0-9]{10,15}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
    
    public static void validateEmail(String email) throws EmailFormatException {
        if (email == null || email.trim().isEmpty()) {
            throw new EmailFormatException("Email cannot be null or empty");
        }
        
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new EmailFormatException("Invalid email format: " + email);
        }
    }
    
    public static void validatePassword(String password) throws ValidationException {
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("Password cannot be null or empty");
        }
        
        if (password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters long");
        }
    }
    
    public static void validateName(String name) throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Name cannot be null or empty");
        }
        
        if (name.trim().length() < 2) {
            throw new ValidationException("Name must be at least 2 characters long");
        }
    }
    
    public static void validatePhone(String phone) throws ValidationException {
        if (phone != null && !phone.trim().isEmpty()) {
            if (!PHONE_PATTERN.matcher(phone.trim()).matches()) {
                throw new ValidationException("Invalid phone number format: " + phone);
            }
        }
    }
    
    public static boolean isValidCSVHeader(String[] expectedHeaders, String[] actualHeaders) {
        if (expectedHeaders.length != actualHeaders.length) {
            return false;
        }
        
        for (int i = 0; i < expectedHeaders.length; i++) {
            if (!expectedHeaders[i].equalsIgnoreCase(actualHeaders[i].trim())) {
                return false;
            }
        }
        return true;
    }
    
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        return input.trim().replaceAll("[<>\"'&]", "");
    }
}