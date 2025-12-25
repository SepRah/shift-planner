package com.example.shiftplanner.api.security;

import com.example.shiftplanner.api.security.dto.AdminUserDTO;
import com.example.shiftplanner.api.security.dto.UpdateUserRolesRequestDTO;
import com.example.shiftplanner.application.security.UserService;
import com.example.shiftplanner.domain.security.UserRole;
import com.example.shiftplanner.infrastructure.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasAnyRole('ADMIN','SYSTEM_ADMIN')")
// ---------------------------
// Class that contains controllers for admin users
// ---------------------------
public class UserAdminController {
    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users in the system.
     *
     * <p>
     * Returns a list of users as {@link AdminUserDTO}s containing only
     * non-sensitive information suitable for administrative views.
     * </p>
     *
     * <p>
     * On success, responds with HTTP 200 (OK).
     * </p>
     *
     * @return ResponseEntity containing a list of all users
     */
    @GetMapping
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    /**
     * Returns the available user roles
     * @return A list of the roles
     */
    @GetMapping("/{userId}/assignable-roles")
    public ResponseEntity<List<String>> getAssignableRoles(
            @PathVariable Long userId) {

        List<String> assignableRoles = userService.getAssignableUserRoles();

        return ResponseEntity.ok(assignableRoles);
    }

    /**
     * Updates the roles assigned to a specific user.
     *
     * <p>
     * Role assignment rules (e.g. SYSTEM_ADMIN restrictions) are enforced
     * in the service layer.
     * </p>
     *
     * <p>
     * On success, responds with HTTP 204 (No Content).
     * </p>
     *
     * @param userId ID of the user whose roles should be updated
     * @param dto request payload containing the new roles
     * @return ResponseEntity with no content
     */
    @PutMapping("/{userId}/roles")
    public ResponseEntity<Void> updateUserRoles(
            @PathVariable Long userId,
            @RequestBody UpdateUserRolesRequestDTO dto) {

        userService.updateUserRoles(userId, dto.roles());
        return ResponseEntity.noContent().build();
    }

    /**
     * Deactivates a user
     * <p>
     *     A deactivated user can no longer authenticate or access protected,
     *     but remains in the db.
     * </p>
     *
     * <p>
     *   On success, responds with HTTP 204 (No Content).
     * </p>
     * @param userId the unique user which should be deactivated
     * @return ResponseEntity with no content
     */
    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long userId) {
        userService.setUserActive(userId, false);
        return ResponseEntity.noContent().build();
    }

    /**
     * Activates a user
     * <p>
     *     Once activated, the user regains the ability to authenticate
     * </p>
     *
     * <p>
     *   On success, responds with HTTP 204 (No Content).
     * </p>
     * @param userId the unique user which should be deactivated
     * @return ResponseEntity with no content
     */
    @PutMapping("/{userId}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long userId) {
        userService.setUserActive(userId, true);
        return ResponseEntity.noContent().build();
    }

}