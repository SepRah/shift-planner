package com.example.shiftplanner.api.task.dto;

import com.example.shiftplanner.domain.task.TimeRange;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignmentCreateDto {
    private Long taskId;
    private Long staffId;
    private TimeRange timeRange;
}
