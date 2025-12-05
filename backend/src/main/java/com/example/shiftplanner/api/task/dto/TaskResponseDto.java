package com.example.shiftplanner.api.task.dto;

import com.example.shiftplanner.domain.task.QualificationLevel;
import com.example.shiftplanner.domain.task.TimeRange;
import lombok.*;

@Getter
@Setter

public class TaskResponseDto {
    private String taskName;
    private String taskDescription;
    private TimeRange taskTimeRange;
    private Long taskEmployeeId;
    private QualificationLevel taskQualificationLevel;
    private Boolean taskComplete;
}
