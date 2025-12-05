package com.example.shiftplanner.application.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserService userService;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:ChangeMe123!}")
    private String adminPassword;

    public AdminInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        if (!userService.usernameExists(adminUsername)) {
            userService.registerAdminUser(adminUsername, adminPassword, "System", "Administrator", 1);
            System.out.println("Default admin user created!");
        } else {
            System.out.println("Admin user already exists, skipping creation.");
        }
    }
}