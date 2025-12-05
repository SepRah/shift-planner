package com.example.shiftplanner.api.task.dto;

import com.example.shiftplanner.domain.task.TimeRange;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter

public class TaskAssignmentCreateDto {

    @NotNull
    private Long taskId;

    @NotNull
    private Long staffId;

    private TimeRange timeRange;

}
