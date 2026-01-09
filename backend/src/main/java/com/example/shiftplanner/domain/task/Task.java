
package com.example.shiftplanner.domain.task;

import com.example.shiftplanner.domain.staff.QualificationLevel;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/**
 * Domain entity representing a task in the shift planning system.
 * <p>
 * A Task has:
 * <ul>
 *   <li>an auto-generated database ID,</li>
 *   <li>a name and optional description,</li>
 *   <li>a required {@link com.example.shiftplanner.domain.staff.QualificationLevel},</li>
 *   <li>timestamps for creation and update,</li>
 *   <li>an active flag for logical deletion.</li>
 * </ul>
 * <p>
 * Business rules:
 * <ul>
 *   <li>Each task must have a qualification level assigned.</li>
 *   <li>Only active tasks are assignable to staff.</li>
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
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QualificationLevel qualificationLevel;

    @Column(updatable = false)
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private boolean defaultTask = false;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}