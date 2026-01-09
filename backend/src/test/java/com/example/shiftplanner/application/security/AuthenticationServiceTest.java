package com.example.shiftplanner.application.security;

import com.example.shiftplanner.api.security.dto.AuthTokenDTO;
import com.example.shiftplanner.api.security.dto.UserLoginRequestDTO;
import com.example.shiftplanner.domain.security.UserRole;
import com.example.shiftplanner.domain.staff.QualificationLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private static SimpleGrantedAuthority role(UserRole role) {
        return new SimpleGrantedAuthority("ROLE_" + role.name());
    }

    private static SimpleGrantedAuthority staff(QualificationLevel q) {
        return new SimpleGrantedAuthority("STAFF_" + q.name());
    }

    @Test
    void shouldAuthenticateUserAndReturnJwt() {
        // Arrange
        UserLoginRequestDTO dto =
                new UserLoginRequestDTO("alice", "password");

        Collection<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("STAFF_MANAGER")
        );

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("alice");
        doReturn(authorities)
                .when(authentication)
                .getAuthorities();

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(jwtService.generateToken(
                eq("alice"),
                eq(Set.of(UserRole.ADMIN)),
                eq(Set.of(QualificationLevel.MANAGER))
        )).thenReturn("jwt-token");

        // Act
        AuthTokenDTO result = authenticationService.login(dto);

        // Assert
        assertEquals("jwt-token", result.token());
        assertEquals("alice", result.username());
        assertEquals(Set.of(UserRole.ADMIN), result.roles());

        verify(jwtService).generateToken(
                "alice",
                Set.of(UserRole.ADMIN),
                Set.of(QualificationLevel.MANAGER)
        );
    }

    @Test
    void shouldPropagateAuthenticationFailure() {
        // Arrange
        UserLoginRequestDTO dto =
                new UserLoginRequestDTO("alice", "wrong");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid"));

        // Act + Assert
        assertThrows(
                BadCredentialsException.class,
                () -> authenticationService.login(dto)
        );

        verify(jwtService, never()).generateToken(any(), any(), any());
    }
}
