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
 * Data Transfer Object für das Updaten einer Aufgabe.
 * */

public class TaskUpdateDto {
    @NotBlank
    private String name;
    private String description;
    private QualificationLevel qualificationLevel;
}