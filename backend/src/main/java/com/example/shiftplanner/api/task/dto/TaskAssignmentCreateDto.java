package com.example.shiftplanner.api.task.dto;

import com.example.shiftplanner.domain.task.TimeRange;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Data Transfer Object (DTO) for creating a new task assignment via the API.
 * <p>
 * Used in POST requests to {@link com.example.shiftplanner.api.task.TaskAssignmentController}.
 * @author Benjamin Traffelet
 * @version 1.0
 * @since 2025-12-20
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignmentCreateDto {

    @NotNull
    private Long taskId;

    @NotNull
    private Long staffId;

    private TimeRange timeRange;

}
