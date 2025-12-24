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
  Authentication and registration endpoints.
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

    /**
     * Registers a new user.
     *
     * <p>
     * Accepts validated registration data, delegates user creation to the
     * {@link UserService}, and returns a DTO representing the newly created user.
     * </p>
     *
     * <p>
     * On success, responds with HTTP 201 (Created) and the created user data.
     * Validation and business rule violations are handled by global exception
     * handlers.
     * </p>
     *
     * @param dto validated user registration data from the request body
     * @return ResponseEntity containing the created user
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRegistrationRequestDTO dto) {
        // Create the user
        User newUser = userService.registerUser(dto);
        UserResponseDTO response = UserMapper.toDTO(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Authenticates a user and issues a JWT.
     *
     * <p>
     * Validates the provided credentials and, on successful authentication,
     * returns a signed JWT that must be included in subsequent requests
     * using the Authorization header.
     * </p>
     *
     * <p>
     * On success, responds with HTTP 200 (OK) and a token payload.
     * Authentication failures are handled by global exception handlers.
     * </p>
     *
     * @param dto login credentials (username and password)
     * @return ResponseEntity containing the authentication token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthTokenDTO> login(@RequestBody UserLoginRequestDTO dto) {

        AuthTokenDTO token = authenticationService.login(dto);
        return ResponseEntity.ok(token);
    }
}