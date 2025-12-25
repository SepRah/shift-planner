package com.example.shiftplanner.api.security;

import com.example.shiftplanner.api.security.dto.ChangePasswordRequestDTO;
import com.example.shiftplanner.application.security.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Changes the password of an authenticated user
     * @param dto The dto contain the old and new password
     * @return ResponseEntity with no content
     */
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequestDTO dto) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        userService.changePassword(username, dto);

        return ResponseEntity.noContent().build();
    }
}

