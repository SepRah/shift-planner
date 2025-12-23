package com.example.shiftplanner.api.security;

import com.example.shiftplanner.api.security.dto.*;
import com.example.shiftplanner.application.security.AuthenticationService;
import com.example.shiftplanner.application.security.UserService;
import com.example.shiftplanner.domain.security.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
/*
  Class that contains all controllers for authentification
 */
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService; // optional if you do JWT

    public AuthController(UserService userService,
                          AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/test")
    public String test() {
        return "Profile is active!";
    }

    // ---------------------------
    // Register a new user
    // ---------------------------
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRegistrationRequestDTO dto) {

        User newUser = userService.registerUser(dto);
        UserResponseDTO response = UserMapper.toDTO(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ---------------------------
    // Login (returns JWT)
    // ---------------------------
    @PostMapping("/login")
    public ResponseEntity<AuthTokenDTO> login(@RequestBody UserLoginRequestDTO dto) {

        AuthTokenDTO token = authenticationService.login(dto);

        return ResponseEntity.ok(token);
    }
}