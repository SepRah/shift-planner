package com.example.shiftplanner.domain.staff;

import com.example.shiftplanner.domain.task.Task;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Domain entity representing a staff member in the shift planning system.
 * <p>
 * A StaffMember has:
 * <ul>
 *   <li>an auto-generated database ID,</li>
 *   <li>a {@link Name} value object,</li>
 *   <li>a {@link QualificationLevel} representing the job level,</li>
 *   <li>an FTE (full-time equivalent) between 0.0 and 1.0.</li>
 * </ul>
 * <p>
 * Business rules:
 * <ul>
 *   <li>FTE must always be in the range [0.0, 1.0].</li>
 *   <li>Assigning a {@link Task} checks that the required qualification level is met.</li>
 * </ul>
 */
@Entity
public class StaffMember {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @NotNull (message = "Name is required")
    @Embedded
    private Name name;   // Value Object

    @Getter
    @Setter
    @NotNull (message = "Qualification level is required")
    private QualificationLevel staffQualificationLevel;

    @Getter
    private double fte;    // Full-time equivalent

    /**
     * Default constructor required by JPA.
     * Should not be used directly in application code.
     */
    protected StaffMember() {} // Required by JPA

    /**
     * Creates a new StaffMember with the given attributes.
     *
     * @param name value object representing the full name
     * @param staffQualificationLevel qualification level (e.g. JUNIOR, SENIOR)
     * @param fte full-time equivalent in the range [0.0, 1.0]
     * @throws IllegalArgumentException if any argument is invalid
     */
    public StaffMember (Name name, QualificationLevel staffQualificationLevel, double fte) {
        if (name == null) throw new IllegalArgumentException("Name is required");
        if (staffQualificationLevel == null) throw new IllegalArgumentException("Qualificationlevel is required");
        if (fte < 0.0 || fte > 1.0 ) throw new IllegalArgumentException("FTE must be between 0.0 and 1.0");

        this.name = name;
        this.staffQualificationLevel = staffQualificationLevel;
        this.fte = fte;
    }

    /**
     * Assigns this staff member to the given task.
     * <p>
     * Before assigning, the qualification level of the task is compared to this staff member's
     * {@link QualificationLevel}. If the staff member does not meet the required level,
     * an {@link IllegalStateException} is thrown.
     *
     * @param task task which should be assigned to this staff member
     * @throws IllegalStateException if the qualification level does not match
     * @throws RuntimeException      if setting the assigned staff on the task fails
     */
    public void  assignTask(Task task) {
        if (task.getQualificationLevel() != this.staffQualificationLevel) throw new IllegalStateException("Qualification level not met");
        try {
            //task.setAssignedStaff(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the full-time equivalent for this staff member.
     * <p>
     * The value must be between 0.0 and 1.0. Otherwise, an exception is thrown.
     *
     * @param fte full-time equivalent
     * @throws IllegalArgumentException if the FTE is outside the range [0.0, 1.0]
     */
    public void setFte(double fte) {
        if (fte < 0.0 || fte > 1.0) throw new IllegalArgumentException("Full-time equivalent must be between 0.0 and 1.0");
        this.fte = fte;
    }
}
