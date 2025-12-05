package com.example.shiftplanner.api.task;

import com.example.shiftplanner.domain.task.Task;
import com.example.shiftplanner.api.task.dto.TaskCreateDto;
import com.example.shiftplanner.api.task.dto.TaskUpdateDto;
import com.example.shiftplanner.api.task.dto.TaskResponseDto;

/**
 * Mapper-Klasse für Task-Entity und DTOs.
 * Wandelt TaskCreateDto/TaskUpdateDto in Task-Entity um und
 * Task-Entity in TaskResponseDto.
 */

public class TaskMapper {

    /**
     * Wandelt ein TaskCreateDto in eine neue Task-Entity um.
     * @param dtoCreate DTO mit Eingabedaten
     * @return neue Task-Entity
     */

    public static Task toEntity(TaskCreateDto dtoCreate) {
        if (dtoCreate == null) return null;

        Task task = new Task();
        task.setName(dtoCreate.getTaskName());
        task.setDescription(dtoCreate.getTaskDescription());
        task.setQualificationLevel(dtoCreate.getTaskQualificationLevel());

        return task;
    }

    /**
     * Update mit TaskUpdateDto eines Tasks.
     * @param dtoUpdate DTO mit neuen Daten
     */

    public static void updateEntity(Task task, TaskUpdateDto dtoUpdate) {
        if (dtoUpdate == null || task == null) return;

        if (dtoUpdate.getTaskName() != null) task.setName(dtoUpdate.getTaskName());
        if (dtoUpdate.getTaskDescription() != null) task.setDescription(dtoUpdate.getTaskDescription());
        if (dtoUpdate.getTaskQualificationLevel() != null) task.setQualificationLevel(dtoUpdate.getTaskQualificationLevel());

    }

    /**
     * Wandelt eine Task-Entity in ein TaskResponseDTO für die API-Antwort um.
     * @param task Entity
     * @return Response DTO
     */

    public static TaskResponseDto toDto(Task task) {
        if (task == null) return null;

        TaskResponseDto dtoResponse = new TaskResponseDto();
        dtoResponse.setTaskName(task.getName());
        dtoResponse.setTaskDescription(task.getDescription());
        dtoResponse.setTaskQualificationLevel(task.getQualificationLevel());

        return dtoResponse;
    }
}
