package com.example.shiftplanner.api.task;

import com.example.shiftplanner.api.task.dto.TaskAssignmentResponseDto;
import com.example.shiftplanner.domain.task.TaskAssignment;

public class TaskAssignmentMapper {

    public static TaskAssignmentResponseDto toDto(TaskAssignment assignment) {
        if (assignment == null) return null;

        TaskAssignmentResponseDto dto = new TaskAssignmentResponseDto();

        dto.setId(assignment.getId());
        dto.setStaffId(assignment.getAssignedStaff().getId());
        dto.setStaffName(assignment.getAssignedStaff().getName());
        dto.setTimeRange(assignment.getTimeRange());
        dto.setCompleted(assignment.getCompleted());

        if (assignment.getTask() != null) {
            dto.setTaskId(assignment.getTask().getId());
            dto.setTaskName(assignment.getTask().getName());
            dto.setTaskDescription(assignment.getTask().getDescription());
            dto.setTaskQualificationLevel(assignment.getTask().getQualificationLevel());
            dto.setTaskCreatedAt(assignment.getTask().getCreatedAt());
            dto.setTaskUpdatedAt(assignment.getTask().getUpdatedAt());
        }

        return dto;
    }
}

