package com.example.shiftplanner.application.security;

import com.example.shiftplanner.api.security.dto.AuthenticationResponse;
import com.example.shiftplanner.api.security.dto.ChangePasswordRequestDTO;
import com.example.shiftplanner.api.security.dto.UserLoginRequestDTO;
import com.example.shiftplanner.api.security.dto.UserRegistrationRequestDTO;
import com.example.shiftplanner.domain.security.User;
import com.example.shiftplanner.domain.security.UserRole;
import com.example.shiftplanner.domain.staff.StaffMember;
import com.example.shiftplanner.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;   // You will create this next
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user.
     */
    public AuthenticationResponse register(UserRegistrationRequestDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(dto.password());

        User user = User.create(
                dto.username(),
                hashedPassword,
                Set.of(UserRole.USER),
                StaffMember.create(dto.firstName(), dto.lastName(), dto.qualification(), dto.fte())
        );

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }

    /**
     * Authenticates a user and returns a JWT.
     */
    public AuthenticationResponse login(UserLoginRequestDTO dto) {

        // Let Spring Security verify password & user existence
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.username(),
                        dto.password()
                )
        );

        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }

    /**
     * Changes the user password.
     */
    public void changePassword(ChangePasswordRequestDTO dto, String currentUsername) {

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Old password does not match");
        }

        user.setPasswordHash(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }
}
