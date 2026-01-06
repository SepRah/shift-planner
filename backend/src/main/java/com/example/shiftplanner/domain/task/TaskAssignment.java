
package com.example.shiftplanner.domain.task;

import com.example.shiftplanner.domain.staff.StaffMember;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/**
 * Domain entity representing the assignment of a task to a staff member.
 * <p>
 * A TaskAssignment has:
 * <ul>
 *   <li>an auto-generated database ID,</li>
 *   <li>a reference to a {@link Task},</li>
 *   <li>a reference to an assigned {@link com.example.shiftplanner.domain.staff.StaffMember},</li>
 *   <li>a {@link TimeRange} for the scheduled period,</li>
 *   <li>a completion status,</li>
 *   <li>timestamps for creation and update.</li>
 * </ul>
 * <p>
 * Business rules:
 * <ul>
 *   <li>Staff members can be assigned to different tasks.</li>
 *   <li>TimeRange must be valid (start before end).</li>
 * </ul>
 * @author Benjamin Traffelet
 * @version 1.0
 * @since 2025-12-20
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_assignments")
public class TaskAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="task_id")
    private Task task;

    @ManyToOne(optional = false)
    @JoinColumn(name= "staff_id")
    private StaffMember assignedStaff;

    @Embedded
    private TimeRange timeRange;

    private Boolean completed = false;

    @Column(updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}

