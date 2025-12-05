package com.example.shiftplanner.domain.staff;

import com.example.shiftplanner.domain.task.Task;
import jakarta.persistence.*;

@Entity
public class StaffMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;     // Value Object

    @ManyToOne
    private Role role;     // Role is another domain class
    private double fte;    // Full-time equivalent

    protected StaffMember() {} // Required by JPA

    public StaffMember (Name name, Role role, double fte) {
        if (name == null) throw new IllegalArgumentException("Name is required");
        if (role == null) throw new IllegalArgumentException("Role is required");
        if (fte < 0.0 || fte > 1.0 ) throw new IllegalArgumentException("FTE must be between 0.0 and 1.0");

        this.name = name;
        this.role = role;
        this.fte = fte;
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
    public double getFte() { return fte; }
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
