package com.example.shiftplanner.api.task.dto;

import com.example.shiftplanner.domain.task.QualificationLevel;
import com.example.shiftplanner.domain.task.TimeRange;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString

/**
 * Data Transfer Object für die Erstellung einer Aufgabe.
 * */

public class TaskCreateDto {
    @NotBlank
    private String taskName;
    private String taskDescription;
    private TimeRange taskTimeRange;
    private Long taskStaffId;
    private QualificationLevel taskQualificationLevel;
    private Boolean taskComplete;
}
