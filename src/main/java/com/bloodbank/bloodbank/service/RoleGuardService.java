package com.bloodbank.bloodbank.service;

import java.util.Arrays;

import org.springframework.stereotype.Service;

@Service
public class RoleGuardService {

    public void requireAnyRole(String roleHeader, String... allowedRoles) {
        if (roleHeader == null || roleHeader.isBlank()) {
            throw new IllegalStateException("Missing user role");
        }

        String normalizedRole = roleHeader.trim().toUpperCase();
        boolean allowed = Arrays.stream(allowedRoles)
                .map(value -> value.trim().toUpperCase())
                .anyMatch(normalizedRole::equals);

        if (!allowed) {
            throw new IllegalStateException("Access denied for role: " + normalizedRole);
        }
    }
}