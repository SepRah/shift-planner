package com.example.shiftplanner.api.task.dto;

import com.example.shiftplanner.domain.task.TimeRange;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    private TimeRange timeRange; // optional at creation, can be set/updated later

}
