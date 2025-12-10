package com.example.shiftplanner.api.task.dto;

import com.example.shiftplanner.domain.task.TimeRange;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignmentUpdateDto {
    
    private TimeRange timeRange;
    private Boolean completed;
    private Long staffId; 
}
