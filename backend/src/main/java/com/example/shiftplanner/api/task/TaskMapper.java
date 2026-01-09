package com.example.shiftplanner.api.task;

import com.example.shiftplanner.domain.task.Task;
import com.example.shiftplanner.api.task.dto.TaskCreateDto;
import com.example.shiftplanner.api.task.dto.TaskResponseDto;
import com.example.shiftplanner.api.task.dto.TaskUpdateDto;

/**
 * Mapper class for converting between Task entities and their DTO representations.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Convert {@link com.example.shiftplanner.api.task.dto.TaskCreateDto} to {@link com.example.shiftplanner.domain.task.Task}</li>
 *   <li>Convert {@link com.example.shiftplanner.domain.task.Task} to {@link com.example.shiftplanner.api.task.dto.TaskResponseDto}</li>
 *   <li>Update {@link com.example.shiftplanner.domain.task.Task} from {@link com.example.shiftplanner.api.task.dto.TaskUpdateDto}</li>
 * </ul>
 * <p>
 * Used by the TaskService and TaskController for API data exchange.
 * @author Benjamin Traffelet
 * @version 1.0
 * @since 2025-12-20
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
    task.setDefaultTask(dtoCreate.getDefaultTask() != null ? dtoCreate.getDefaultTask() : false);
    task.setActive(dtoCreate.getActive() != null ? dtoCreate.getActive() : true);
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
    dtoResponse.setId(task.getId());
    dtoResponse.setName(task.getName());
    dtoResponse.setDescription(task.getDescription());
    dtoResponse.setQualificationLevel(task.getQualificationLevel());
    dtoResponse.setActive(task.isActive());
    dtoResponse.setDefaultTask(task.isDefaultTask());
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
        if (dtoUpdate.getActive() != null) task.setActive(dtoUpdate.getActive());
        if (dtoUpdate.getDefaultTask() != null) task.setDefaultTask(dtoUpdate.getDefaultTask());
    }
}
