package com.example.shiftplanner.application.security;

import com.example.shiftplanner.api.security.UserMapper;
import com.example.shiftplanner.api.security.dto.ChangePasswordRequestDTO;
import com.example.shiftplanner.api.security.dto.UserRegistrationRequestDTO;
import com.example.shiftplanner.domain.security.User;
import com.example.shiftplanner.domain.security.UserRole;
import com.example.shiftplanner.domain.staff.Name;
import com.example.shiftplanner.domain.staff.QualificationLevel;
import com.example.shiftplanner.domain.staff.StaffMember;

import com.example.shiftplanner.exception.InvalidPasswordException;
import com.example.shiftplanner.infrastructure.StaffMemberRepository;
import com.example.shiftplanner.infrastructure.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StaffMemberRepository staffmemberRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       StaffMemberRepository staffmemberRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.staffmemberRepository = staffmemberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new default user.
     */
    public User registerDefaultUser(UserRegistrationRequestDTO dto) {
        String encodedPassword = passwordEncoder.encode(dto.password());
        User user = UserMapper.toEntity(dto, encodedPassword);
        return userRepository.save(user);
    }

    /**
     * Registers a new user with a given role.
     */
    public User registerUser(UserRegistrationRequestDTO dto
    ) {

        if (userRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("Username already taken");
        }

        if (staffmemberRepository.existsByNameFirstNameAndNameLastName(dto.firstName(), dto.lastName())) {
            throw new IllegalArgumentException("Staffmember already exists");
        }

        String encodedPassword = passwordEncoder.encode(dto.password());

        // Create Staffmember linked to user
        StaffMember staffmember = new StaffMember(new Name(dto.firstName(), dto.lastName()), QualificationLevel.NONE, dto.fte());

        User user = new User(dto.username(), encodedPassword, Set.of(UserRole.USER));
        user.setStaffmember(staffmember);

        return userRepository.save(user);
    }

    // ----------------------------
    // Private admin creation method
    // ----------------------------
    protected void registerAdminUser(String username,
                                     String rawPassword) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Admin username already exists");
        }

        String encoded = passwordEncoder.encode(rawPassword);
        StaffMember staff = new StaffMember(new Name("System", "Administrator"), QualificationLevel.MANAGER, 1);

        User admin = new User(username, encoded, Set.of(UserRole.ADMIN));
        admin.setStaffmember(staff);

        userRepository.save(admin);
    }

    /**
     * Admin-only: assign additional USER roles.
     */
    public User assignUserRoles(Long targetUserId, Set<UserRole> newRoles, User actingUser) {

        // Ensure the acting user is admin
        if (!actingUser.getRoles().contains(UserRole.ADMIN)) {
            throw new SecurityException("Only admin users can assign user roles.");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        targetUser.getRoles().addAll(newRoles);
        return userRepository.save(targetUser);
    }

    /**
     * Checks if a username exits
     */
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean staffExists(String firstName, String lastName) {
        return staffmemberRepository.existsByNameFirstNameAndNameLastName(firstName, lastName);
    }

    /**
     * Lookup user by username (used by controllers or other services).
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    /**
     * Lets the user change its password.
     */
    public void changePassword(String username, ChangePasswordRequestDTO dto) {
        User user = findByUsername(username);

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPasswordHash())) {
            throw new InvalidPasswordException("Old password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }
}
