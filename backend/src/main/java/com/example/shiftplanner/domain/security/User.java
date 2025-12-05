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
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    @Getter
    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Setter
    @Getter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "staff_id")
    private StaffMember staffmember;

    protected User() {} // JPA requirement

    public User(String username, String passwordHash, Set<UserRole> roles) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.roles = roles;
    }

    // Factory
    public static User create(String username, String password, Set<UserRole> roles, StaffMember staffmember) {
        User user = new User();
        user.username = username;
        user.passwordHash = password;
        user.roles = roles;
        user.staffmember = staffmember;
        return user;
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