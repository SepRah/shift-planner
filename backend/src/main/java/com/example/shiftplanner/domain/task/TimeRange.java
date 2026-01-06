package com.example.shiftplanner.domain.task;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.Instant;
import lombok.*;

/**
 * Value object representing a time range for a task assignment.
 * <p>
 * Used as an embeddable component in {@link TaskAssignment}.
 * <ul>
 *   <li>Consists of a start and end {@link java.time.Instant}.</li>
 * </ul>
 * <p>
 * Business rules:
 * <ul>
 *   <li>Start must be before end.</li>
 *   <li>Both start and end must be non-null for a valid range.</li>
 * </ul>
 * @author Benjamin Traffelet
 * @version 1.0
 * @since 2025-12-20
 */

@Getter
@Setter
@ToString
@Embeddable
public class TimeRange {
    @Column(name = "start_time")
    private Instant start;

    @Column(name = "end_time")
    private Instant end;

    public TimeRange() {}

    public TimeRange(Instant start, Instant end) {
        this.start = start;
        this.end = end;
    }

    public boolean isValid() {
        return start != null && end != null && start.isBefore(end);
    }
}
