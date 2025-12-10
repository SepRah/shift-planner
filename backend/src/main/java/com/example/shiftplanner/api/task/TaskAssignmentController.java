package com.example.shiftplanner.api.task;

import com.example.shiftplanner.api.task.dto.TaskAssignmentCreateDto;
import com.example.shiftplanner.api.task.dto.TaskAssignmentUpdateDto;
import com.example.shiftplanner.api.task.dto.TaskAssignmentResponseDto;
import com.example.shiftplanner.application.task.TaskAssignmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-assignments")
public class TaskAssignmentController {

    private final TaskAssignmentService service;

    public TaskAssignmentController(TaskAssignmentService service) {
        this.service = service;
    }

    @PostMapping
    public TaskAssignmentResponseDto create(@RequestBody TaskAssignmentCreateDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public TaskAssignmentResponseDto update(@PathVariable Long id,
                                            @RequestBody TaskAssignmentUpdateDto dto) {
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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

