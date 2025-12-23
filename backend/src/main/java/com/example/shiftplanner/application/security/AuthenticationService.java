package com.example.shiftplanner.application.security;

import com.example.shiftplanner.api.security.dto.*;
import com.example.shiftplanner.domain.security.UserRole;

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

        Set<UserRole> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(r -> r.replace("ROLE_", ""))
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());

        // JWT should be generated from username / auth, not entity
        String token = jwtService.generateToken(username, roles);

        return new AuthTokenDTO(token, username, roles);

    }

}
