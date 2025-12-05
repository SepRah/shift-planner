package com.example.shiftplanner.application.task;

import com.example.shiftplanner.api.task.TaskMapper;
import com.example.shiftplanner.api.task.dto.TaskCreateDto;
import com.example.shiftplanner.api.task.dto.TaskResponseDto;
import com.example.shiftplanner.application.staff.StaffMemberService;
import com.example.shiftplanner.domain.task.Task;
import com.example.shiftplanner.infrastructure.security.TaskRepository;

public class TaskService {

    private final TaskRepository taskRepository;
    private final StaffMemberService staffMemberService;

    public TaskService(TaskRepository taskRepository, StaffMemberService staffMemberService) {
        this.taskRepository = taskRepository;
        this.staffMemberService = staffMemberService;
    }

    public TaskResponseDto create(TaskCreateDto dto) {
        Task task = TaskMapper.toEntity(dto);
        task = taskRepository.save(task);
        return TaskMapper.toDto(task);
    }



}
