package com.bloodbank.bloodbank.service;

import java.util.Set;

public final class BloodGroupValidator {

    private static final Set<String> VALID_GROUPS = Set.of("O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+");

    private BloodGroupValidator() {
    }

    public static String normalizeAndValidate(String bloodGroup) {
        if (bloodGroup == null) {
            throw new IllegalArgumentException("Blood group is required");
        }

        String normalized = bloodGroup.trim().toUpperCase().replace(" ", "");

        if (!VALID_GROUPS.contains(normalized)) {
            throw new IllegalArgumentException("Invalid blood group. Use one of: O+, O-, A+, A-, B+, B-, AB+, AB-");
        }

        return normalized;
    }
}