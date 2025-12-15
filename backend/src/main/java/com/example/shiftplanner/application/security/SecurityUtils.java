package com.example.shiftplanner.application.security;

import com.example.shiftplanner.domain.security.User;
import com.example.shiftplanner.exception.AccessDeniedException;
import com.example.shiftplanner.infrastructure.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
/**
 * Utility class for accessing security-related information
 * from the Spring Security context.
 *
 * Centralizes logic for resolving the currently authenticated user.
 */
public class SecurityUtils {

    private final UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns the currently authenticated user.
     *
     * Uses Spring Security's SecurityContext to obtain the Authentication
     * object, extracts the username, and loads the User entity from the database.
     *
     * @throws AccessDeniedException if:
     *  - no authentication exists
     *  - the user is not authenticated
     *  - the user cannot be found in the database
     */
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("No authenticated user");
        }

        String username = auth.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new AccessDeniedException("User not found"));
    }
}