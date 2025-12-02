package com.example.shiftplanner.domain.staff;

import com.example.shiftplanner.domain.task.Task;
import jakarta.persistence.*;

@Entity
public class Staffmember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;     // Value Object

    @ManyToOne
    private Role role;     // Role is another domain class

    protected void StaffMember() {} // Required by JPA

    public void StaffMember(Name name, Role role) {
        if (name == null) throw new IllegalArgumentException("Name is required");
        if (role == null) throw new IllegalArgumentException("Role is required");

        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }
    public boolean can(Permission permission) {
        return role.hasPermission(permission);
    }

    public void assignShift(Task shift) {
        if (!can(Permission.ASSIGN_SHIFT)) {
            throw new IllegalStateException("Not allowed");
        }
        shift.assignTo(this);
    }


    // Business behavior
    public void changeName(Name newName) {
        if (newName == null) throw new IllegalArgumentException("Name is required");
        this.name = newName;
    }

    public void changeRole(Role newRole) {
        if (newRole == null) throw new IllegalArgumentException("Role is required");
        this.role = newRole;
    }
}
