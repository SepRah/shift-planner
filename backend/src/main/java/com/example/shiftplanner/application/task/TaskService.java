package com.example.shiftplanner.application.task;

import com.example.shiftplanner.api.task.TaskMapper;
import com.example.shiftplanner.api.task.dto.TaskCreateDto;
import com.example.shiftplanner.api.task.dto.TaskResponseDto;
import com.example.shiftplanner.domain.task.Task;
import com.example.shiftplanner.infrastructure.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponseDto create(TaskCreateDto dto) {
        Task task = TaskMapper.toEntity(dto);
        task = taskRepository.save(task);
        return TaskMapper.toDto(task);
    }
}
