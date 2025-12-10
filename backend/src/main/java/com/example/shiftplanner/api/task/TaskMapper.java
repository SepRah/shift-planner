package com.example.shiftplanner.api.task;

import com.example.shiftplanner.domain.task.Task;
import com.example.shiftplanner.api.task.dto.TaskCreateDto;
import com.example.shiftplanner.api.task.dto.TaskResponseDto;
import com.example.shiftplanner.api.task.dto.TaskUpdateDto;

/**
 * Mapper-Klasse für Task-Entity und DTOs.
 * Wandelt TaskCreateDto in Task-Entity um und
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
        task.setName(dtoCreate.getName());
        task.setDescription(dtoCreate.getDescription());
        task.setQualificationLevel(dtoCreate.getQualificationLevel());

        return task;
    }

    /**
     * Wandelt eine Task-Entity in ein TaskResponseDTO für die API-Antwort um.
     * @param task Entity
     * @return Response DTO
     */

    public static TaskResponseDto toDto(Task task) {
        if (task == null) return null;

        TaskResponseDto dtoResponse = new TaskResponseDto();
        dtoResponse.setName(task.getName());
        dtoResponse.setDescription(task.getDescription());
        dtoResponse.setQualificationLevel(task.getQualificationLevel());

        return dtoResponse;
    }

    /**
     * Aktualisiert eine Task-Entity mit Werten aus einem TaskUpdateDto.
     * Nur Felder, die im DTO gesetzt sind, werden übernommen.
     */
    public static void updateEntity(Task task, TaskUpdateDto dtoUpdate) {
        if (task == null || dtoUpdate == null) return;
        if (dtoUpdate.getName() != null) task.setName(dtoUpdate.getName());
        if (dtoUpdate.getDescription() != null) task.setDescription(dtoUpdate.getDescription());
        if (dtoUpdate.getQualificationLevel() != null) task.setQualificationLevel(dtoUpdate.getQualificationLevel());
    }
}
