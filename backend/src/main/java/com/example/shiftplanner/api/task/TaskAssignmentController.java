package com.example.shiftplanner.api.task;

import com.example.shiftplanner.api.task.dto.TaskAssignmentCreateDto;
import com.example.shiftplanner.api.task.dto.TaskAssignmentUpdateDto;
import com.example.shiftplanner.api.task.dto.TaskAssignmentResponseDto;
import com.example.shiftplanner.application.task.TaskAssignmentService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * REST controller for managing task assignments.
 * <p>
 * Exposes endpoints for creating, updating, retrieving, and deleting task assignments.
 * Delegates business logic to {@link com.example.shiftplanner.application.task.TaskAssignmentService}.
 * <p>
 * API endpoints:
 * <ul>
 *   <li>GET /api/task-assignments - List all assignments</li>
 *   <li>GET /api/task-assignments/{id} - Get assignment by ID</li>
 *   <li>GET /api/task-assignments/task/{taskId} - List assignments for a task</li>
 *   <li>GET /api/task-assignments/staff/{staffId} - List assignments for a staff member</li>
 *   <li>POST /api/task-assignments - Create new assignment</li>
 *   <li>PUT /api/task-assignments/{id} - Update assignment</li>
 *   <li>DELETE /api/task-assignments/{id} - Delete assignment</li>
 * </ul>
 * @author Benjamin Traffelet
 * @version 1.0
 * @since 2025-12-20
 */

@RestController
@RequestMapping("/api/task-assignments")
public class TaskAssignmentController {

    private final TaskAssignmentService service;

    public TaskAssignmentController(TaskAssignmentService service) {
        this.service = service;
    }

    @PostMapping
    public TaskAssignmentResponseDto create(@RequestBody @Valid TaskAssignmentCreateDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public TaskAssignmentResponseDto update(@PathVariable Long id,
                                            @RequestBody @Valid TaskAssignmentUpdateDto dto) {
        return service.update(id, dto);
    }

    @GetMapping("/{id}")
    public TaskAssignmentResponseDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/task/{taskId}")
    public List<TaskAssignmentResponseDto> getByTask(@PathVariable Long taskId) {
        return service.getByTask(taskId);
    }

    @GetMapping("/staff/{staffId}")
    public List<TaskAssignmentResponseDto> getByStaff(@PathVariable Long staffId) {
        return service.getByStaff(staffId);
    }

    @GetMapping
    public List<TaskAssignmentResponseDto> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

