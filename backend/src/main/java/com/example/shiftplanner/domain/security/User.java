package com.example.shiftplanner.domain.security;

import com.example.shiftplanner.domain.staff.StaffMember;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();
    @OneToOne
    private StaffMember staffMember;

    protected User() {} // JPA requirement

    public User(String username, String passwordHash, Set<UserRole> roles) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.roles = roles;
    }

    // Getters & behavior
    public boolean hasRole(UserRole role) {
        return roles.contains(role);
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }
}