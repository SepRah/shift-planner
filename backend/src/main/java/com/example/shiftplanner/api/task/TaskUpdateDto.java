package com.example.shiftplanner.api.task;

import com.example.shiftplanner.domain.task.QualificationLevel;
import com.example.shiftplanner.domain.task.TimeRange;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString

public class TaskUpdateDto {
    @NotBlank
    private String taskName;
    private String taskDescription;
    private TimeRange taskTimeRange;
    private Long taskEmployeeId;
    private QualificationLevel taskQualificationLevel;
    private Boolean taskComplete;
}