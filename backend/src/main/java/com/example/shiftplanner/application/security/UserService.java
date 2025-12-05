package com.example.shiftplanner.application.security;

import com.example.shiftplanner.domain.security.User;
import com.example.shiftplanner.domain.security.UserRole;
import com.example.shiftplanner.domain.staff.Name;
import com.example.shiftplanner.domain.staff.Role;
import com.example.shiftplanner.domain.staff.StaffMember;

import com.example.shiftplanner.infrastructure.security.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
//    private final StaffmemberRepository staffmemberRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
//                       StaffmemberRepository staffmemberRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
//        this.staffmemberRepository = staffmemberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user with a given role.
     */
    public User registerUser(String username,
                             String rawPassword,
                             String firstName,
                             String lastName,
                             double fte
    ) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }

//        if (staffmemberRepository.existsByFirstNameAndLastName(firstName, lastName)) {
//            throw new IllegalArgumentException("Staffmember already exists");
//        }

        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Create Staffmember linked to user
        StaffMember staffmember = new StaffMember(new Name(firstName, lastName), staffRole, fte);

        User user = new User(username, encodedPassword, Set.of(UserRole.USER));
        user.setStaffmember(staffmember);

        return userRepository.save(user);
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

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

//    public boolean staffExists(String firstName, String lastName) {
//        return staffmemberRepository.existsByFirstNameAndLastName(firstName, lastName);
//    }


    /**
     * Lookup user by username (used by controllers or other services).
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // ----------------------------
    // Private admin creation method
    // ----------------------------
    protected User registerAdminUser(String username,
                                     String rawPassword,
                                     String firstName,
                                     String lastName,
                                     double fte) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Admin username already exists");
        }

        String encoded = passwordEncoder.encode(rawPassword);
        StaffMember staff = new StaffMember(new Name(firstName, lastName), new Role(), fte);

        User admin = new User(username, encoded, Set.of(UserRole.ADMIN));
        admin.setStaffmember(staff);

        return userRepository.save(admin);
    }
}
