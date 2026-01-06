package com.example.shiftplanner.api.task.dto;

import com.example.shiftplanner.domain.task.TimeRange;
import lombok.*;

/**
 * Data Transfer Object (DTO) for updating an existing task assignment via the API.
 * <p>
 * Used in PUT requests to {@link com.example.shiftplanner.api.task.TaskAssignmentController}.
 * @author Benjamin Traffelet
 * @version 1.0
 * @since 2025-12-20
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignmentUpdateDto {
    private TimeRange timeRange;
    private Boolean completed;
    private Long staffId;
}
