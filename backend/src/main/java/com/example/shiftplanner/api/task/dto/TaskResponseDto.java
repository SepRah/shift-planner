package com.example.shiftplanner.api.task.dto;

import com.example.shiftplanner.domain.staff.QualificationLevel;
import lombok.*;


/**
 * Data Transfer Object für die Ausgabe einer Aufgabe.
 * */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {
    private String name;
    private String description;
    private QualificationLevel qualificationLevel;
}
