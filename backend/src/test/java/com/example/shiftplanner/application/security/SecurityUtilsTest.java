package com.example.shiftplanner.application.security;

import com.example.shiftplanner.domain.security.User;
import com.example.shiftplanner.domain.security.UserRole;
import com.example.shiftplanner.exception.AccessDeniedException;
import com.example.shiftplanner.infrastructure.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityUtilsTest {

    @Mock
    private UserRepository userRepository;

    private SecurityUtils securityUtils;

    @BeforeEach
    void setUp() {
        securityUtils = new SecurityUtils(userRepository);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnCurrentUserWhenAuthenticated() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("macto");

        SecurityContextHolder.getContext().setAuthentication(auth);

        User user = new User("macto", "hashed", Set.of(UserRole.USER));

        when(userRepository.findByUsername("macto"))
                .thenReturn(Optional.of(user));

        // Act
        User result = securityUtils.getCurrentUser();

        // Assert
        assertEquals("macto", result.getUsername());
    }

    @Test
    void shouldThrowWhenNoAuthentication() {
        // Arrange
        SecurityContextHolder.clearContext();

        // Act + Assert
        assertThrows(
                AccessDeniedException.class,
                () -> securityUtils.getCurrentUser()
        );
    }

    @Test
    void shouldThrowWhenNotAuthenticated() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);

        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act + Assert
        assertThrows(
                AccessDeniedException.class,
                () -> securityUtils.getCurrentUser()
        );
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("ghost");

        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userRepository.findByUsername("ghost"))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                AccessDeniedException.class,
                () -> securityUtils.getCurrentUser()
        );
    }
}