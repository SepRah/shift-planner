package com.example.shiftplanner.api.security;

import com.example.shiftplanner.api.security.dto.*;
import com.example.shiftplanner.application.security.AuthenticationService;
import com.example.shiftplanner.application.security.UserService;
import com.example.shiftplanner.domain.security.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService; // optional if you do JWT

    public AuthController(UserService userService,
                          AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    // ---------------------------
    // Register a new user
    // ---------------------------
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRegistrationRequestDTO dto) {

        User newUser = userService.registerDefaultUser(dto);
        UserResponseDTO response = UserMapper.toDTO(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ---------------------------
    // Login (returns JWT or session token)
    // ---------------------------
    @PostMapping("/login")
    public ResponseEntity<AuthTokenDTO> login(@RequestBody UserLoginRequestDTO dto) {

        AuthTokenDTO token = authenticationService.login(dto);

        return ResponseEntity.ok(token);
    }

    // ---------------------------
    // Change password (authenticated user)
    // ---------------------------
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDTO dto) {

        // Get the currently authenticated username
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // Call service method
        userService.changePassword(currentUsername, dto);

        return ResponseEntity.ok("Password changed successfully");
    }
}