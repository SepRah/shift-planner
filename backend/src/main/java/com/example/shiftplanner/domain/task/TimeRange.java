package com.example.shiftplanner.domain.task;

import jakarta.persistence.Embeddable;
import java.time.Instant;
import lombok.*;


@Getter
@Setter
@ToString
@Embeddable
public class TimeRange {
    private Instant start;
    private Instant end;

    public TimeRange() {}

    public TimeRange(Instant start, Instant end) {
        this.start = start;
        this.end = end;
    }

}
