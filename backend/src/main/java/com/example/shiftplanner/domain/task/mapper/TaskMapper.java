package com.example.shiftplanner.domain.task.mapper;

import com.example.shiftplanner.domain.task.Task;
import com.example.shiftplanner.domain.staff.StaffMember;
import com.example.shiftplanner.api.task.dto.TaskCreateDto;
import com.example.shiftplanner.api.task.dto.TaskUpdateDto;
import com.example.shiftplanner.api.task.dto.TaskResponseDto;

public class TaskMapper {
    public static Task toEntity(TaskCreateDto dtoCreate) {
        if (dtoCreate == null) return null;

        Task task = new Task();
        task.setName(dtoCreate.getTaskName());
        task.setDescription(dtoCreate.getTaskDescription());
        task.setQualificationLevel(dtoCreate.getTaskQualificationLevel());
        task.setTimeRange(dtoCreate.getTaskTimeRange());
        task.setCompleted(false);

        if (dtoCreate.getTaskEmployeeId() != null) {
            StaffMember staff = new StaffMember();
            staff.setId(dtoCreate.getTaskEmployeeId());
            task.setAssignedStaff(staff);
        }

        return task;
    }

    public static void updateEntity(Task task, TaskUpdateDto dtoUpdate) {
        if (dtoUpdate == null || task == null) return;

        if (dtoUpdate.getTaskName() != null) task.setName(dtoUpdate.getTaskName());
        if (dtoUpdate.getTaskDescription() != null) task.setDescription(dtoUpdate.getTaskDescription());
        if (dtoUpdate.getTaskQualificationLevel() != null) task.setQualificationLevel(dtoUpdate.getTaskQualificationLevel());
        if (dtoUpdate.getTaskTimeRange() != null) task.setTimeRange(dtoUpdate.getTaskTimeRange());
        if (dtoUpdate.getTaskComplete() != null) task.setCompleted(dtoUpdate.getTaskComplete());

        if (dtoUpdate.getTaskEmployeeId() != null) {
            StaffMember staff = new StaffMember();
            staff.setId(dtoUpdate.getTaskEmployeeId());
            task.setAssignedStaff(staff);
        }
    }

    public static TaskResponseDto toDto(Task task) {
        if (task == null) return null;

        TaskResponseDto dtoResponse = new TaskResponseDto();
        dtoResponse.setTaskName(task.getName());
        dtoResponse.setTaskDescription(task.getDescription());
        dtoResponse.setTaskTimeRange(task.getTimeRange());
        dtoResponse.setTaskQualificationLevel(task.getQualificationLevel());
        dtoResponse.setTaskComplete(task.getCompleted());

        if (task.getAssignedStaff() != null) {
            dtoResponse.setTaskEmployeeId(task.getAssignedStaff().getId());
        }

        return dtoResponse;
    }

}
