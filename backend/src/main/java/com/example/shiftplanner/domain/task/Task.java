package com.example.shiftplanner.domain.task;

import com.example.shiftplanner.domain.staff.Staffmember;
import jakarta.persistence.*;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TimeRange timeRange;

    @ManyToOne(fetch = FetchType.LAZY)
    private Staffmember assignedStaff;

    protected Task() {} // Required by JPA

    public Task(TimeRange timeRange) {
        if (timeRange == null) {
            throw new IllegalArgumentException("Time range is required");
        }
        this.timeRange = timeRange;
    }

    public Long getId() {
        return id;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public Staffmember getAssignedStaff() {
        return assignedStaff;
    }

    public void assignTo(Staffmember staff) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff member is required");
        }
        this.assignedStaff = staff;
    }

//    public boolean overlapsWith(Shift other) {
//        return this.timeRange.overlapsWith(other.timeRange);
//    }
}