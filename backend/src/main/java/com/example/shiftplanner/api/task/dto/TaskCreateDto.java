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
 * Data Transfer Object (DTO) for creating a new task via the API.
 * <p>
 * Used in POST requests to {@link com.example.shiftplanner.api.task.TaskController}.
 * @author Benjamin Traffelet
 * @version 1.0
 * @since 2025-12-20
 */

public class TaskCreateDto {
    @NotBlank
    private String name;
    private String description;
    private QualificationLevel qualificationLevel;
    private Boolean active = true;
}
