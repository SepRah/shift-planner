package com.example.shiftplanner.domain.staff;

import com.example.shiftplanner.domain.task.Task;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class StaffMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;     // Value Object

    //@ManyToOne ?
    private QualificationLevel staffQualificationLevel;

    private double fte;    // Full-time equivalent

    protected StaffMember() {} // Required by JPA

    public StaffMember (Name name, QualificationLevel staffQualificationLevel, double fte) {
        if (name == null) throw new IllegalArgumentException("Name is required");
        if (staffQualificationLevel == null) throw new IllegalArgumentException("Qualificationlevel is required");
        if (fte < 0.0 || fte > 1.0 ) throw new IllegalArgumentException("FTE must be between 0.0 and 1.0");

        this.name = name;
        this.staffQualificationLevel = staffQualificationLevel;
        this.fte = fte;
    }

    /**
     * Inkludiert StaffMember zu einem Task, zuerst wird die QualificationLevel überprüft
     * @param task Task welcher zu StaffMember zugewiesen werden möchte
     */
    public void  assignTask(Task task) {
        if (task.getQualificationLevel() != this.staffQualificationLevel) throw new IllegalStateException("Qualificationlevel not met");
        try {
            task.setAssignedStaff(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void setName(Name newName) {
        if (newName == null) throw new IllegalArgumentException("Name is required");
        this.name = newName;
    }

    public void setQualificationLevel(QualificationLevel qualificationLevel) {
        if (qualificationLevel == null) throw new IllegalArgumentException("QualificationLevel is required");
        this.staffQualificationLevel = qualificationLevel;
    }

    public void setFte(double fte) {
        if (fte < 0.0 || fte > 1.0) throw new IllegalArgumentException("Full Time Equivalent must be between 0.0 and 1.0");
        this.fte = fte;
    }
}
