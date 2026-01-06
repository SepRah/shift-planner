package com.example.shiftplanner.api.task;

import com.example.shiftplanner.api.task.dto.TaskAssignmentResponseDto;
import com.example.shiftplanner.api.task.dto.TaskAssignmentCreateDto;
import com.example.shiftplanner.api.task.dto.TaskAssignmentUpdateDto;
import com.example.shiftplanner.domain.task.TaskAssignment;
import com.example.shiftplanner.domain.task.Task;
import com.example.shiftplanner.domain.staff.StaffMember;

/**
 * Mapper class for converting between TaskAssignment entities and their DTO representations.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Convert {@link com.example.shiftplanner.domain.task.TaskAssignment} to {@link com.example.shiftplanner.api.task.dto.TaskAssignmentResponseDto}</li>
 *   <li>Convert {@link com.example.shiftplanner.api.task.dto.TaskAssignmentCreateDto} to {@link com.example.shiftplanner.domain.task.TaskAssignment}</li>
 *   <li>Update {@link com.example.shiftplanner.domain.task.TaskAssignment} from {@link com.example.shiftplanner.api.task.dto.TaskAssignmentUpdateDto}</li>
 * </ul>
 * <p>
 * Used by the TaskAssignmentService and TaskAssignmentController for API data exchange.
 * @author Benjamin Traffelet
 * @version 1.0
 * @since 2025-12-20
 */

public class TaskAssignmentMapper {

    public static TaskAssignmentResponseDto toDto(TaskAssignment assignment) {
        if (assignment == null) return null;

        TaskAssignmentResponseDto dto = new TaskAssignmentResponseDto();
        dto.setId(assignment.getId());
        dto.setStaffId(assignment.getAssignedStaff() != null ? assignment.getAssignedStaff().getId() : null);
        dto.setTimeRange(assignment.getTimeRange());
        dto.setCompleted(assignment.getCompleted());

        // Task-Infos immer setzen, auch wenn Task inaktiv ist
        Task task = assignment.getTask();
        if (task != null) {
            dto.setTaskId(task.getId());
            dto.setTaskName(task.getName());
            dto.setTaskDescription(task.getDescription());
            dto.setTaskQualificationLevel(task.getQualificationLevel());
            dto.setTaskCreatedAt(task.getCreatedAt());
            dto.setTaskUpdatedAt(task.getUpdatedAt());
        }

        // Staff-Infos immer setzen
        StaffMember staff = assignment.getAssignedStaff();
        if (staff != null && staff.getName() != null) {
            dto.setStaffFirstName(staff.getName().getFirstName());
            dto.setStaffLastName(staff.getName().getLastName());
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

