package com.example.shiftplanner.domain.task.dto;

import com.example.shiftplanner.domain.task.QualificationLevel;
import com.example.shiftplanner.domain.task.TimeRange;

public class TaskCreateDTO {
    private String taskName;
    private String taskDescription;
    private TimeRange taskTimeRange;
    private Long taskEmployeeId;
    private QualificationLevel taskQualificationLevel;

}
