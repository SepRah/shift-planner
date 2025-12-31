package com.example.shiftplanner.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class StaffPermission {

    public boolean canAccessUserAdministration(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a ->
                        a.equals("ROLE_SYSTEM_ADMIN") ||
                                a.equals("ROLE_ADMIN") ||
                                a.equals("STAFF_MANAGER") ||
                                a.equals("STAFF_SENIOR")
                );
    }
}