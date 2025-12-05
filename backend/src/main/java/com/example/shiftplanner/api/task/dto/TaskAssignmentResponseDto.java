package com.example.shiftplanner.api.task.dto;

import com.example.shiftplanner.domain.staff.QualificationLevel;
import com.example.shiftplanner.domain.task.TimeRange;

import java.time.Instant;

public class TaskAssignmentResponseDto {

    private Long taskId;
    private String taskName;
    private String taskDescription;
    private QualificationLevel taskQualificationLevel;
    private Instant taskCreatedAt;
    private Instant taskUpdatedAt;

    private Long staffId;
    private String staffName;
    private TimeRange timeRange;
    private Boolean completed;

}
