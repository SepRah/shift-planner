package com.example.shiftplanner.api.security;

import com.example.shiftplanner.api.security.dto.AdminUserDTO;
import com.example.shiftplanner.api.security.dto.UpdateUserRolesRequestDTO;
import com.example.shiftplanner.application.security.UserService;
import com.example.shiftplanner.infrastructure.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasAnyRole('ADMIN','SYSTEM_ADMIN')")
// ---------------------------
// Class that contains controllers for admin users
// ---------------------------
public class UserAdminController {
    private final UserService userService;

    public UserAdminController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
    }

    @PutMapping("/{userId}/roles")
    @PreAuthorize("hasAnyRole('ADMIN','SYSTEM_ADMIN')")
    public ResponseEntity<Void> updateUserRoles(
            @PathVariable Long userId,
            @RequestBody UpdateUserRolesRequestDTO dto) {

        userService.updateUserRoles(userId, dto.roles());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long userId) {
        userService.setUserActive(userId, false);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long userId) {
        userService.setUserActive(userId, true);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

}