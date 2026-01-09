package com.example.shiftplanner.application.security;

import com.example.shiftplanner.domain.security.UserRole;
import com.example.shiftplanner.domain.staff.QualificationLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private final long expirationMs = 60_000;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        // Manually inject values normally provided by @Value
        String secret = "this-is-a-very-long-secret-key-that-is-at-least-256-bits";
        ReflectionTestUtils.setField(jwtService, "secret", secret);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", expirationMs);
    }

    @Test
    void shouldGenerateAndValidateToken() {
        String token = jwtService.generateToken(
                "alice",
                Set.of(UserRole.ADMIN, UserRole.USER),
                Set.of(QualificationLevel.MANAGER)
        );

        assertTrue(jwtService.isTokenValid(token));
        assertEquals("alice", jwtService.extractUsername(token));
    }

    @Test
    void shouldExtractRolesAndStaffQualifications() {
        String token = jwtService.generateToken(
                "bob",
                Set.of(UserRole.USER),
                Set.of(QualificationLevel.MANAGER)
        );

        assertEquals(
                Set.of(UserRole.USER),
                jwtService.extractSystemRoles(token)
        );

        assertEquals(
                Set.of(QualificationLevel.MANAGER),
                jwtService.extractStaffQualifications(token)
        );
    }

    @Test
    void shouldRejectTokenSignedWithDifferentSecret() {
        String token = jwtService.generateToken(
                "alice",
                Set.of(UserRole.USER),
                Set.of()
        );

        JwtService otherService = new JwtService();
        ReflectionTestUtils.setField(otherService, "secret", "different-secret-different-secret-different-secret");
        ReflectionTestUtils.setField(otherService, "jwtExpirationMs", expirationMs);

        assertFalse(otherService.isTokenValid(token));
    }

    @Test
    void shouldRejectExpiredToken() {
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs", 1L);

        String token = jwtService.generateToken(
                "alice",
                Set.of(UserRole.USER),
                Set.of()
        );

        // Small sleep to ensure expiration
        try { Thread.sleep(5); } catch (InterruptedException ignored) {}

        assertFalse(jwtService.isTokenValid(token));
    }
}
