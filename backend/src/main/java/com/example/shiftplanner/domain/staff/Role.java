package com.example.shiftplanner.domain.staff;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // "Admin", "Manager", "Employee"

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Permission> permissions;

    protected Role() {}

    public Role(String name, Set<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    public String getName() { return name; }
}