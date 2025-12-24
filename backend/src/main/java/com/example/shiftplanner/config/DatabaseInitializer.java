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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@Profile("dev")
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner initTestUsers(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            // create test users and staffmember
            StaffMember adminStaff = new StaffMember(new Name("Alice", "Admin"), QualificationLevel.MANAGER, 1.0);
            User admin = new User(
                    "adminUser",
                    encoder.encode("admin123"),
                    Set.of(UserRole.SYSTEM_ADMIN)
            );
            admin.setStaffmember(adminStaff);
            repo.save(admin);

            StaffMember managerStaff = new StaffMember(new Name("Bob", "Manager"), QualificationLevel.SENIOR, 0.9);
            User manager = new User(
                    "managerUser",
                    encoder.encode("manager1"),
                    Set.of(UserRole.ADMIN)
            );
            manager.setStaffmember(managerStaff);
            repo.save(manager);

            StaffMember employeeStaff = new StaffMember(new Name("Eve", "Employee"), QualificationLevel.JUNIOR, 0.5);
            User employee = new User(
                    "employeeUser",
                    encoder.encode("employee1"),
                    Set.of(UserRole.USER)
            );
            employee.setStaffmember(employeeStaff);
            repo.save(employee);
            System.out.println("Test users created!");
        };
    }
}