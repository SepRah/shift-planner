package com.example.shiftplanner.application.security;

import com.example.shiftplanner.api.security.dto.AdminUserDTO;
import com.example.shiftplanner.exception.RegistrationException;
import com.example.shiftplanner.api.security.dto.ChangePasswordRequestDTO;
import com.example.shiftplanner.api.security.dto.UserRegistrationRequestDTO;
import com.example.shiftplanner.domain.security.User;
import com.example.shiftplanner.domain.security.UserRole;
import com.example.shiftplanner.domain.staff.Name;
import com.example.shiftplanner.domain.staff.QualificationLevel;
import com.example.shiftplanner.domain.staff.StaffMember;

import com.example.shiftplanner.exception.AccessDeniedException;
import com.example.shiftplanner.exception.InvalidPasswordException;
import com.example.shiftplanner.exception.UserNotFoundException;
import com.example.shiftplanner.infrastructure.StaffMemberRepository;
import com.example.shiftplanner.infrastructure.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StaffMemberRepository staffmemberRepository;
    private final PasswordEncoder passwordEncoder;
    private SecurityUtils securityUtils;

    public UserService(UserRepository userRepository,
                       StaffMemberRepository staffmemberRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.staffmemberRepository = staffmemberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user with the staff role "none".
     */
    @Transactional
    public User registerUser(UserRegistrationRequestDTO dto
    ) {
        // Throw errors when the user or the staffmember already exists
        if (userRepository.existsByUsername(dto.username())) {
            throw new RegistrationException("Username already taken");
        }
        if (staffmemberRepository.existsByNameFirstNameAndNameLastName(dto.firstName(), dto.lastName())) {
            throw new RegistrationException("Staffmember already exists");
        }

        String encodedPassword = passwordEncoder.encode(dto.password());

        // Create Staffmember linked to user
        StaffMember staffmember = new StaffMember(new Name(dto.firstName(), dto.lastName()), QualificationLevel.NONE, dto.fte());

        User user = new User(dto.username(), encodedPassword, Set.of(UserRole.USER));
        user.setStaffmember(staffmember);

        return userRepository.save(user);
    }

    /**
     * Private admin creation method
     * @param username the admin username
     * @param rawPassword The raw pw
     */
    @Transactional
    protected void registerAdminUser(String username,
                                     String rawPassword) {

        if (userRepository.existsByUsername(username)) {
            throw new RegistrationException("Admin username already exists");
        }

        String encoded = passwordEncoder.encode(rawPassword);
        StaffMember staff = new StaffMember(new Name("System", "Administrator"), QualificationLevel.MANAGER, 1);

        User admin = new User(username, encoded, Set.of(UserRole.ADMIN));
        admin.setStaffmember(staff);

        userRepository.save(admin);
    }

    /**
     * Admin-only: assign/update additional USER roles.
     * @param targetUserId the targeted user to be changed
     * @param newRoles The newly set of roles
     */
    @Transactional
    public void updateUserRoles(Long targetUserId, Set<UserRole> newRoles) {
        // Get acting user
        User actingUser = securityUtils.getCurrentUser();

        // Must be ADMIN or SYSTEM_ADMIN
        boolean isAdmin =
                actingUser.getRoles().contains(UserRole.ADMIN) ||
                        actingUser.getRoles().contains(UserRole.SYSTEM_ADMIN);

        if (!isAdmin) {
            throw new AccessDeniedException("Not allowed to update roles");
        }

        // Only SYSTEM_ADMIN may assign SYSTEM_ADMIN
        if (newRoles.contains(UserRole.SYSTEM_ADMIN) &&
                !actingUser.getRoles().contains(UserRole.SYSTEM_ADMIN)) {
            throw new AccessDeniedException(
                    "Only SYSTEM_ADMIN can assign SYSTEM_ADMIN role"
            );
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Prevent self-demotion
        if (actingUser.getId().equals(targetUserId) &&
                !newRoles.contains(UserRole.ADMIN) &&
                !newRoles.contains(UserRole.SYSTEM_ADMIN)) {
            throw new AccessDeniedException("You cannot remove your own admin role");
        }

        // Replace roles atomically
        targetUser.getRoles().clear();
        targetUser.getRoles().addAll(newRoles);
    }

    /**
     * Checks if a username exits
     * @param username the user's unique username
     * @return a bool
     */
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Lookup user by username (used by controllers or other services).
     * @param username the user's unique username
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    /**
     * Lets the user change its password.
     *  @param username the user's unique username
     *  @param dto the request dto
     */
    @Transactional
    public void changePassword(String username, ChangePasswordRequestDTO dto) {
        User user = findByUsername(username);

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPasswordHash())) {
            throw new InvalidPasswordException("Old password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }

    /**
     * Toggles user activity.
     * @param userId the user's unique identifier
     * @param activity whether the user should be active or not
     */
    @Transactional
    public void setUserActive(Long userId, boolean activity) {
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        // Change the active property
        targetUser.setActive(activity);
    }

    /**
     * Returns all users for the admin dashboard.
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<AdminUserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new AdminUserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getStaffmember().getName().getFirstName(),
                        user.getStaffmember().getName().getLastName(),
                        user.isActive(),
                        user.getRoles()
                ))
                .toList();
    }
}
