package com.example.shiftplanner.api.task.dto;

import com.example.shiftplanner.domain.staff.QualificationLevel;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

/**
 * Data Transfer Object für die Erstellung einer Aufgabe.
 * */

public class TaskCreateDto {
    @NotBlank
    private String name;
    private String description;
    private QualificationLevel qualificationLevel;
}
