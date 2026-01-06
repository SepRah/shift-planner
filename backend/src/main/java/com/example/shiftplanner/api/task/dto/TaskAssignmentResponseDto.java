package com.example.shiftplanner.api.task.dto;

import com.example.shiftplanner.domain.staff.QualificationLevel;
import com.example.shiftplanner.domain.task.TimeRange;
import lombok.*;
import java.time.Instant;

/**
 * Data Transfer Object (DTO) for returning task assignment data to API clients.
 * <p>
 * Contains all relevant fields for a task assignment, including task and staff details, time range, and completion status.
 * Used in responses from {@link com.example.shiftplanner.api.task.TaskAssignmentController}.
 * @author Benjamin Traffelet
 * @version 1.0
 * @since 2025-12-20
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignmentResponseDto {
    private Long id;
    private Long taskId;
    private String taskName;
    private String taskDescription;
    private QualificationLevel taskQualificationLevel;
    private Instant taskCreatedAt;
    private Instant taskUpdatedAt;
    private Long staffId;
    private String staffFirstName;
    private String staffLastName;
    private TimeRange timeRange;
    private Boolean completed;
}
