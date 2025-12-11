package com.example.shiftplanner.config;

import com.example.shiftplanner.domain.security.User;
import com.example.shiftplanner.domain.security.UserRole;
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
            repo.save(new User("adminUser",
                    encoder.encode("admin123"),
                    Set.of(UserRole.SYSTEM_ADMIN)));

            repo.save(new User("managerUser",
                    encoder.encode("manager1"),
                    Set.of(UserRole.ADMIN)));

            repo.save(new User("employeeUser",
                    encoder.encode("employee1"),
                    Set.of(UserRole.USER)));

            System.out.println("Test users created.");
        };
    }
}