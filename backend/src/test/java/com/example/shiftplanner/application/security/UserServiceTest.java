package com.example.shiftplanner.application.security;

import com.example.shiftplanner.api.security.dto.ChangePasswordRequestDTO;
import com.example.shiftplanner.api.security.dto.UserRegistrationRequestDTO;
import com.example.shiftplanner.domain.security.User;
import com.example.shiftplanner.domain.security.UserRole;
import com.example.shiftplanner.exception.AccessDeniedException;
import com.example.shiftplanner.exception.InvalidPasswordException;
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
    private StaffMemberRepository staffmemberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private UserService userService;

    // Recurrent code wrapped in functions
    private User existingUser() {
        return new User("john", "HASHED_OLD", Set.of(UserRole.USER));
    }

    private void userExists(User user) {
        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));
    }

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
    void shouldCreateAdminUserSuccessfully() {
        // given
        when(userRepository.existsByUsername("admin"))
                .thenReturn(false);

        when(passwordEncoder.encode("secret"))
                .thenReturn("ENCODED");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        userService.registerAdminUser("admin", "secret");

        // then
        verify(userRepository).save(argThat(user ->
                user.getUsername().equals("admin")
                        && user.getRoles().contains(UserRole.ADMIN)
//                        && user.getStaffmember().getQualificationLevel() == QualificationLevel.MANAGER
                        && "System".equals(user.getStaffmember().getName().getFirstName())
        ));
    }

    @Test
    void shouldFailWhenOldPasswordIsIncorrect() {
        // given
        User user = existingUser();
        userExists(user);

        when(passwordEncoder.matches("wrongOld", "HASHED_OLD"))
                .thenReturn(false);

        ChangePasswordRequestDTO dto =
                new ChangePasswordRequestDTO("wrongOld", "newPassword");

        // when / then
        InvalidPasswordException ex = assertThrows(
                InvalidPasswordException.class,
                () -> userService.changePassword("john", dto)
        );

        assertEquals("Old password is incorrect", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        // given
        User user = existingUser();;
        userExists(user);

        when(passwordEncoder.matches("oldPw", "HASHED_OLD"))
                .thenReturn(true);

        when(passwordEncoder.encode("newPw"))
                .thenReturn("HASHED_NEW");

        ChangePasswordRequestDTO dto =
                new ChangePasswordRequestDTO("oldPw", "newPw");

        // when
        userService.changePassword("john", dto);

        // then
        assertEquals("HASHED_NEW", user.getPasswordHash());
        verify(userRepository).save(user);
    }

    @Test
    void shouldFailWhenActingUserIsNotAdmin() {
        // given
        User actingUser = new User("user", "pw", Set.of(UserRole.USER));
        when(securityUtils.getCurrentUser()).thenReturn(actingUser);

        // when / then
        assertThrows(AccessDeniedException.class, () ->
                userService.updateUserRoles(2L, Set.of(UserRole.ADMIN))
        );

        verify(userRepository, never()).findById(any());
    }

    @Test
    void shouldFailWhenAdminAssignsSystemAdminRole() {
        // given
        User actingUser = new User("admin", "pw", Set.of(UserRole.ADMIN));
        when(securityUtils.getCurrentUser()).thenReturn(actingUser);

        // when / then
        assertThrows(AccessDeniedException.class, () ->
                userService.updateUserRoles(2L, Set.of(UserRole.SYSTEM_ADMIN))
        );

        verify(userRepository, never()).findById(any());
    }

//    @Test
//    void updateUserRoles() {
//        // given
//        userService.registerAdminUser("admin", "secret");
//    }


}