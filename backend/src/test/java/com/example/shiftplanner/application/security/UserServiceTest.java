package com.example.shiftplanner.application.security;

import com.example.shiftplanner.api.security.dto.UserRegistrationRequestDTO;
import com.example.shiftplanner.domain.security.User;
import com.example.shiftplanner.domain.security.UserRole;
import com.example.shiftplanner.infrastructure.StaffMemberRepository;
import com.example.shiftplanner.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StaffMemberRepository staffmemberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        // given
        UserRegistrationRequestDTO dto =
                new UserRegistrationRequestDTO("john", "pw", "John", "Doe", 1.0);

        when(userRepository.existsByUsername("john")).thenReturn(true);

        // when / then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(dto)
        );

        assertEquals("Username already taken", ex.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenStaffMemberAlreadyExists() {
        // given
        UserRegistrationRequestDTO dto =
                new UserRegistrationRequestDTO("john", "pw", "John", "Doe", 1.0);

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(staffmemberRepository
                .existsByNameFirstNameAndNameLastName("John", "Doe"))
                .thenReturn(true);

        // when / then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(dto)
        );

        assertEquals("Staffmember already exists", ex.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // given
        UserRegistrationRequestDTO dto =
                new UserRegistrationRequestDTO("john", "rawPw", "John", "Doe", 0.8);

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(staffmemberRepository
                .existsByNameFirstNameAndNameLastName("John", "Doe"))
                .thenReturn(false);

        when(passwordEncoder.encode("rawPw"))
                .thenReturn("ENCODED_PW");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        User result = userService.registerUser(dto);

        // then
        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("ENCODED_PW", result.getPassword());
        assertTrue(result.getRoles().contains(UserRole.USER));

        assertNotNull(result.getStaffmember());
        assertEquals("John", result.getStaffmember().getName().getFirstName());
        assertEquals("Doe", result.getStaffmember().getName().getLastName());
        assertEquals(0.8, result.getStaffmember().getFte());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerAdminUser() {
    }

    @Test
    void updateUserRoles() {
    }

    @Test
    void findByUsername() {
    }

    @Test
    void changePassword() {
    }
}