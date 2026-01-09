package com.example.shiftplanner.config;

import com.example.shiftplanner.domain.security.User;
import com.example.shiftplanner.domain.security.UserRole;
import com.example.shiftplanner.domain.staff.Name;
import com.example.shiftplanner.domain.staff.QualificationLevel;
import com.example.shiftplanner.domain.staff.StaffMember;
import com.example.shiftplanner.infrastructure.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@Profile("dev")
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner initTestUsers(UserRepository repo, PasswordEncoder encoder) {
        return args -> {

            createUserSafely(
                    repo,
                    encoder,
                    "adminUser",
                    "admin123",
                    Set.of(UserRole.SYSTEM_ADMIN),
                    new StaffMember(
                            new Name("Alice", "Admin"),
                            QualificationLevel.MANAGER,
                            1.0
                    )
            );

            createUserSafely(
                    repo,
                    encoder,
                    "managerUser",
                    "manager1",
                    Set.of(UserRole.ADMIN),
                    new StaffMember(
                            new Name("Bob", "Manager"),
                            QualificationLevel.SENIOR,
                            0.9
                    )
            );

            createUserSafely(
                    repo,
                    encoder,
                    "employeeUser",
                    "employee1",
                    Set.of(UserRole.USER),
                    new StaffMember(
                            new Name("Eve", "Employee"),
                            QualificationLevel.JUNIOR,
                            0.5
                    )
            );

            System.out.println("Test users initialized (duplicates skipped).");
        };
    }
    private void createUserSafely(
            UserRepository repo,
            PasswordEncoder encoder,
            String username,
            String rawPassword,
            Set<UserRole> roles,
            StaffMember staffMember
    ) {
        try {
            User user = new User(
                    username,
                    encoder.encode(rawPassword),
                    roles
            );
            user.setStaffmember(staffMember);
            repo.save(user);

            System.out.println("Created test user: " + username);
        } catch (DataIntegrityViolationException e) {
            // Unique constraint on username hit
            System.out.println("Test user already exists, skipping: " + username);
        }
    }
}