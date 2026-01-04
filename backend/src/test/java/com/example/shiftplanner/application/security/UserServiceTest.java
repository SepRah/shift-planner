package com.example.shiftplanner.application.security;

import com.example.shiftplanner.api.security.dto.ChangePasswordRequestDTO;
import com.example.shiftplanner.api.security.dto.UserRegistrationRequestDTO;
import com.example.shiftplanner.domain.security.User;
import com.example.shiftplanner.domain.security.UserRole;
import com.example.shiftplanner.exception.AccessDeniedException;
import com.example.shiftplanner.exception.InvalidPasswordException;
import com.example.shiftplanner.exception.RegistrationException;
import com.example.shiftplanner.infrastructure.StaffMemberRepository;
import com.example.shiftplanner.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StaffMemberRepository staffMemberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private UserService userService;

    // ---------- helpers ----------

    private User existingUser() {
        return new User("john", "HASHED_OLD", Set.of(UserRole.USER));
    }

    private void mockUserExists(User user) {
        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));
    }

    // ---------- registration ----------

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        UserRegistrationRequestDTO dto =
                new UserRegistrationRequestDTO("john", "password123", "John", "Doe", 1.0);

        when(userRepository.existsByUsername("john")).thenReturn(true);

        RegistrationException ex = assertThrows(
                RegistrationException.class,
                () -> userService.registerUser(dto)
        );

        assertEquals("Username already taken", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenStaffMemberAlreadyExists() {
        UserRegistrationRequestDTO dto =
                new UserRegistrationRequestDTO("john", "password123", "John", "Doe", 1.0);

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(staffMemberRepository
                .existsByNameFirstNameAndNameLastName("John", "Doe"))
                .thenReturn(true);

        RegistrationException ex = assertThrows(
                RegistrationException.class,
                () -> userService.registerUser(dto)
        );

        assertEquals("Staffmember already exists", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        UserRegistrationRequestDTO dto =
                new UserRegistrationRequestDTO("john", "password123", "John", "Doe", 0.8);

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(staffMemberRepository
                .existsByNameFirstNameAndNameLastName("John", "Doe"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("ENCODED_PW");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.registerUser(dto);

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("ENCODED_PW", result.getPasswordHash());
        assertTrue(result.getRoles().contains(UserRole.USER));

        assertNotNull(result.getStaffmember());
        assertEquals("John", result.getStaffmember().getName().getFirstName());
        assertEquals("Doe", result.getStaffmember().getName().getLastName());
        assertEquals(0.8, result.getStaffmember().getFte());

        verify(userRepository).save(any(User.class));
    }

    // ---------- admin creation ----------

    @Test
    void shouldCreateAdminUserSuccessfully() {
        when(userRepository.existsByUsername("admin")).thenReturn(false);
        when(passwordEncoder.encode("secureAdminPw"))
                .thenReturn("ENCODED_ADMIN");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        userService.registerAdminUser("admin", "secureAdminPw");

        verify(userRepository).save(argThat(user ->
                user.getUsername().equals("admin")
                        && user.getRoles().contains(UserRole.SYSTEM_ADMIN)
                        && "System".equals(user.getStaffmember().getName().getFirstName())
        ));
    }

    // ---------- password change ----------

    @Test
    void shouldFailWhenOldPasswordIsIncorrect() {
        User user = existingUser();
        mockUserExists(user);

        when(passwordEncoder.matches("wrongOldPw", "HASHED_OLD"))
                .thenReturn(false);

        ChangePasswordRequestDTO dto =
                new ChangePasswordRequestDTO("wrongOldPw", "newPassword123");

        InvalidPasswordException ex = assertThrows(
                InvalidPasswordException.class,
                () -> userService.changePassword("john", dto)
        );

        assertEquals("Old password is incorrect", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        User user = existingUser();
        mockUserExists(user);

        when(passwordEncoder.matches("oldPassword123", "HASHED_OLD"))
                .thenReturn(true);
        when(passwordEncoder.matches("newPassword123", "HASHED_OLD"))
                .thenReturn(false);
        when(passwordEncoder.encode("newPassword123"))
                .thenReturn("HASHED_NEW");

        ChangePasswordRequestDTO dto =
                new ChangePasswordRequestDTO("oldPassword123", "newPassword123");

        userService.changePassword("john", dto);

        assertEquals("HASHED_NEW", user.getPasswordHash());
        verify(userRepository).save(user);
    }

    // ---------- role updates ----------

    @Test
    void shouldFailWhenActingUserIsNotAdmin() {
        User actingUser = new User("user", "pw", Set.of(UserRole.USER));
        when(securityUtils.getCurrentUser()).thenReturn(actingUser);

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(new User("target", "pw", Set.of(UserRole.USER))));

        assertThrows(AccessDeniedException.class, () ->
                userService.updateUserRoles(2L, Set.of(UserRole.ADMIN))
        );
    }

    @Test
    void shouldFailWhenAdminAssignsSystemAdminRole() {
        User actingUser = new User("admin", "pw", Set.of(UserRole.ADMIN));
        when(securityUtils.getCurrentUser()).thenReturn(actingUser);

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(new User("target", "pw", Set.of(UserRole.USER))));

        assertThrows(AccessDeniedException.class, () ->
                userService.updateUserRoles(2L, Set.of(UserRole.SYSTEM_ADMIN))
        );
    }
}
