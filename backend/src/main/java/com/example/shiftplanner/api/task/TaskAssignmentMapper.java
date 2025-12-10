package com.example.shiftplanner.api.task;

import com.example.shiftplanner.api.task.dto.TaskAssignmentResponseDto;
import com.example.shiftplanner.api.task.dto.TaskAssignmentCreateDto;
import com.example.shiftplanner.api.task.dto.TaskAssignmentUpdateDto;
import com.example.shiftplanner.domain.task.TaskAssignment;
import com.example.shiftplanner.domain.task.Task;
import com.example.shiftplanner.domain.staff.StaffMember;

public class TaskAssignmentMapper {

    public static TaskAssignmentResponseDto toDto(TaskAssignment assignment) {
        if (assignment == null) return null;

        TaskAssignmentResponseDto dto = new TaskAssignmentResponseDto();
        dto.setId(assignment.getId());
        dto.setStaffId(assignment.getAssignedStaff().getId());
        dto.setStaffName(String.valueOf(assignment.getAssignedStaff().getName()));
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

    /**
     * Erstellt ein neues TaskAssignment aus den geladenen Entitäten und dem CreateDto.
     * Task und StaffMember müssen im Service geladen werden!
     */
    public static TaskAssignment toEntity(TaskAssignmentCreateDto dto, Task task, StaffMember staff) {
        if (dto == null || task == null || staff == null) return null;
        TaskAssignment assignment = new TaskAssignment();
        assignment.setTask(task);
        assignment.setAssignedStaff(staff);
        assignment.setTimeRange(dto.getTimeRange());
        assignment.setCompleted(false);
        return assignment;
    }

    /**
     * Aktualisiert ein bestehendes TaskAssignment mit Werten aus einem UpdateDto.
     * StaffMember muss ggf. im Service geladen und gesetzt werden!
     */
    public static void updateEntity(TaskAssignment assignment, TaskAssignmentUpdateDto dtoUpdate, StaffMember staff) {
        if (assignment == null || dtoUpdate == null) return;
        if (dtoUpdate.getTimeRange() != null) assignment.setTimeRange(dtoUpdate.getTimeRange());
        if (dtoUpdate.getCompleted() != null) assignment.setCompleted(dtoUpdate.getCompleted());
        if (dtoUpdate.getStaffId() != null && staff != null) assignment.setAssignedStaff(staff);
    }
}

