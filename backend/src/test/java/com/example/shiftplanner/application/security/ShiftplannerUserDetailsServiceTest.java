package com.example.shiftplanner.application.security;

import com.example.shiftplanner.domain.security.User;
import com.example.shiftplanner.domain.security.UserRole;
import com.example.shiftplanner.domain.staff.Name;
import com.example.shiftplanner.domain.staff.QualificationLevel;
import com.example.shiftplanner.domain.staff.StaffMember;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import static org.mockito.Mockito.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ShiftplannerUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private ShiftplannerUserDetailsService userDetailsService;

    @Test
    void shouldLoadUserWithRolesAndStaffQualification() {
        // Arrange
        User user = new User("macto", "hashed", Set.of(UserRole.ADMIN));
        StaffMember staff = new StaffMember(new Name("Toni", "Maccaroni"), QualificationLevel.MANAGER, 0.9);
        user.setStaffmember(staff);

        when(userService.findByUsername("alice")).thenReturn(user);

        // Act
        UserDetails details = userDetailsService.loadUserByUsername("alice");

        // Assert
        assertEquals("macto", details.getUsername());
        assertEquals("hashed", details.getPassword());

        assertTrue(details.getAuthorities().contains(
                new SimpleGrantedAuthority("ROLE_ADMIN")
        ));
        assertTrue(details.getAuthorities().contains(
                new SimpleGrantedAuthority("STAFF_MANAGER")
        ));
    }

    @Test
    void shouldLoadUserWithoutStaffQualification() {
        // Arrange
        User user = new User("macto", "hashed", Set.of(UserRole.ADMIN));

        when(userService.findByUsername("macto")).thenReturn(user);

        // Act
        UserDetails details = userDetailsService.loadUserByUsername("macto");

        // Assert
        assertTrue(details.getAuthorities().contains(
                new SimpleGrantedAuthority("ROLE_ADMIN")
        ));

        assertFalse(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().startsWith("STAFF_"))
        );
    }
}
