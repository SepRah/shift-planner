package com.example.shiftplanner.api.task.dto;

import com.example.shiftplanner.domain.staff.QualificationLevel;
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
    private QualificationLevel taskQualificationLevel;
}
