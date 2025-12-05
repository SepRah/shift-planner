package com.example.shiftplanner.domain.security;

import com.example.shiftplanner.domain.staff.StaffMember;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;
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

    @Getter
    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();
    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "staff_id")
    private StaffMember staffmember;

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

    public Collection<UserRole> getRoles() {
        return  roles;
    }

}