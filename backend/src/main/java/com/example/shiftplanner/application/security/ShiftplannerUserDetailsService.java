package com.example.shiftplanner.application.security;

import com.example.shiftplanner.domain.security.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShiftplannerUserDetailsService implements UserDetailsService {

    private final UserService userService; // inject your service here

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return userService.findByUsername(username); // User implements UserDetails
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}