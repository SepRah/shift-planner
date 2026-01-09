package com.example.shiftplanner.application.security;

import com.example.shiftplanner.api.security.dto.*;
import com.example.shiftplanner.domain.security.UserRole;

import com.example.shiftplanner.domain.staff.QualificationLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Authenticates a user and returns a JWT.
     */
    public AuthTokenDTO login(UserLoginRequestDTO dto) {

        // Authenticate user (this also loads UserDetails)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.username(),
                        dto.password()
                )
        );

        // Get authenticated principal; works for UserDetails & OAuth2
        String username = authentication.getName();

        // System roles (ROLE_*)
        Set<UserRole> systemRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.replace("ROLE_", ""))
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());


        // Staff qualifications (STAFF_*)
        Set<QualificationLevel> staffQualifications =
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .filter(a -> a.startsWith("STAFF_"))
                        .map(a -> a.replace("STAFF_", ""))
                        .map(QualificationLevel::valueOf)
                        .collect(Collectors.toSet());

        String token = jwtService.generateToken(
                username,
                systemRoles,
                staffQualifications
        );

        return new AuthTokenDTO(token, username, systemRoles);
    }
}
