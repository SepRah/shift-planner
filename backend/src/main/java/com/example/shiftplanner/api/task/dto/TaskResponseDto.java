package com.example.shiftplanner.api.task.dto;

import com.example.shiftplanner.domain.staff.QualificationLevel;
import com.example.shiftplanner.domain.task.TimeRange;
import lombok.*;

@Getter
@Setter

/**
 * Data Transfer Object für die Ausgabe einer Aufgabe.
 * */

public class TaskResponseDto {
    private String taskName;
    private String taskDescription;
    private TimeRange taskTimeRange;
    private QualificationLevel taskQualificationLevel;
    private Boolean taskComplete;
}
