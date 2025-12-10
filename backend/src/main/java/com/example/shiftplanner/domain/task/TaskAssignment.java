package com.example.shiftplanner.domain.task;

import com.example.shiftplanner.domain.staff.StaffMember;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter
@Setter
@ToString
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

    public TaskAssignment(Task task, StaffMember assignedStaff) {
        this.task = task;
        this.assignedStaff = assignedStaff;
        this.completed = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public TaskAssignment(Task task, StaffMember assignedStaff, TimeRange timeRange) {
        this.task = task;
        this.assignedStaff = assignedStaff;
        this.timeRange = timeRange;
        this.completed = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public void updateTimeRange(TimeRange newTimeRange) {
        this.timeRange = newTimeRange;
        this.updatedAt = Instant.now();
    }
}

