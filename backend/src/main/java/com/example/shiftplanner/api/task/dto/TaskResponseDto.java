package com.example.shiftplanner.api.task.dto;

import com.example.shiftplanner.domain.staff.QualificationLevel;
import lombok.*;


/**
 * Data Transfer Object (DTO) for returning task data to API clients.
 * <p>
 * Contains all relevant fields for a task, including qualification level and active status.
 * Used in responses from {@link com.example.shiftplanner.api.task.TaskController}.
 * @author B
 * @version 1.0
 * @since 2025-12-20
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {
    private Long id;
    private String name;
    private String description;
    private QualificationLevel qualificationLevel;
    private Boolean active;
}
