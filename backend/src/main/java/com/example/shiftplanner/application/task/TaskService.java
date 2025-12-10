package com.example.shiftplanner.application.task;

import com.example.shiftplanner.api.task.TaskMapper;
import com.example.shiftplanner.api.task.dto.TaskCreateDto;
import com.example.shiftplanner.api.task.dto.TaskResponseDto;
import com.example.shiftplanner.api.task.dto.TaskUpdateDto;
import com.example.shiftplanner.domain.task.Task;
import com.example.shiftplanner.exception.TaskNotFoundException;
import com.example.shiftplanner.infrastructure.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public TaskResponseDto update(Long id, TaskUpdateDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(TaskNotFoundException::new);
        TaskMapper.updateEntity(task, dto);
        task = taskRepository.save(task);
        return TaskMapper.toDto(task);
    }

    public TaskResponseDto get(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(TaskNotFoundException::new);
        return TaskMapper.toDto(task);
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException();
        }
        taskRepository.deleteById(id);
    }

    public List<TaskResponseDto> getAll() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }
}
