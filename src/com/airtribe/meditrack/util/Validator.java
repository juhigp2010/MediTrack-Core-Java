package com.airtribe.meditrack.util;

import com.airtribe.meditrack.exception.InvalidDataException;

public class Validator {
    public static void validateAge(int age) {
        if (age < 0 || age > 120) {
            throw new InvalidDataException("Age must be between 0 and 120. Given: " + age);
        }
    }

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException("Name cannot be null or empty.");
        }
        if (name.trim().length() < 2) {
            throw new InvalidDataException("Name must be at least 2 characters.");
        }
    }

    public static void validateAilment(String ailment) {
        if (ailment == null || ailment.trim().isEmpty()) {
            throw new InvalidDataException("Ailment cannot be null or empty.");
        }
    }

    public static void validateFee(double fee) {
        if (fee < 0) {
            throw new InvalidDataException("Fee cannot be negative. Given: " + fee);
        }
    }

    public static void validateDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new InvalidDataException("Date cannot be null or empty.");
        }
        if (!DateUtil.isValidDate(dateStr)) {
            throw new InvalidDataException("Date must follow format 'yyyy-MM-dd' or 'yyyy-MM-dd HH:mm'. Given: " + dateStr);
        }
    }
}
