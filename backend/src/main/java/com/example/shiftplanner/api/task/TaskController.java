package com.example.shiftplanner.api.task;

import com.example.shiftplanner.api.task.dto.TaskCreateDto;
import com.example.shiftplanner.api.task.dto.TaskUpdateDto;
import com.example.shiftplanner.api.task.dto.TaskResponseDto;
import com.example.shiftplanner.application.task.TaskService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * REST controller for managing tasks.
 * <p>
 * Exposes endpoints for creating, updating, retrieving, and deleting tasks.
 * Delegates business logic to {@link com.example.shiftplanner.application.task.TaskService}.
 * <p>
 * API endpoints:
 * <ul>
 *   <li>GET /api/tasks - List all active tasks</li>
 *   <li>GET /api/tasks/all - List all tasks including inactive</li>
 *   <li>GET /api/tasks/{id} - Get task by ID</li>
 *   <li>POST /api/tasks - Create new task</li>
 *   <li>PUT /api/tasks/{id} - Update task</li>
 *   <li>DELETE /api/tasks/{id} - Delete task</li>
 * </ul>
 * @author Bejamin Traffelet
 * @version 1.0
 * @since 2025-12-20
 */

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @GetMapping("/all")
    public List<TaskResponseDto> getAllInclInactive() {
        return taskService.getAllTasksInclInactive();
    }

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponseDto create(@RequestBody @Valid TaskCreateDto dto) {
        return taskService.create(dto);
    }

    @GetMapping("/{id}")
    public TaskResponseDto get(@PathVariable Long id) {
        return taskService.get(id);
    }

    @GetMapping
    public List<TaskResponseDto> getAll() {
        return taskService.getAll();
    }

    @PutMapping("/{id}")
    public TaskResponseDto update(@PathVariable Long id, @RequestBody @Valid TaskUpdateDto dto) {
        return taskService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    @PatchMapping("/{id}/status")
    public TaskResponseDto updateActiveStatus(@PathVariable Long id, @RequestBody java.util.Map<String, Object> statusPayload) {
    if (!statusPayload.containsKey("active")) {
        throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Missing 'active' field");
    }
    Boolean active = Boolean.valueOf(String.valueOf(statusPayload.get("active")));
    return taskService.updateActiveStatus(id, active);
    }
}
