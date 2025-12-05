package com.example.shiftplanner.api.task;

import com.example.shiftplanner.api.task.dto.TaskCreateDto;
import com.example.shiftplanner.api.task.dto.TaskResponseDto;
import com.example.shiftplanner.application.task.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponseDto create(@RequestBody TaskCreateDto dto) {
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
    public TaskResponseDto update(@PathVariable Long id, @RequestBody TaskCreateDto dto) {
        return taskService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
