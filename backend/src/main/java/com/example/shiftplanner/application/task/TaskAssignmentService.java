package com.example.shiftplanner.application.task;

import com.example.shiftplanner.api.task.dto.TaskAssignmentCreateDto;
import com.example.shiftplanner.api.task.dto.TaskAssignmentResponseDto;
import com.example.shiftplanner.api.task.dto.TaskAssignmentUpdateDto;
import com.example.shiftplanner.api.task.TaskAssignmentMapper;
import com.example.shiftplanner.domain.staff.StaffMember;
import com.example.shiftplanner.domain.task.Task;
import com.example.shiftplanner.domain.task.TaskAssignment;
import com.example.shiftplanner.exception.AssignmentNotFoundException;
import com.example.shiftplanner.exception.StaffMemberNotFoundException;
import com.example.shiftplanner.exception.TaskNotFoundException;
import com.example.shiftplanner.infrastructure.StaffMemberRepository;
import com.example.shiftplanner.infrastructure.TaskAssignmentRepository;
import com.example.shiftplanner.infrastructure.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskAssignmentService {

    private final TaskAssignmentRepository assignmentRepository;
    private final TaskRepository taskRepository;
    private final StaffMemberRepository staffMemberRepository;

    // findStaffMemberById aus StaffMemberRepository


    public TaskAssignmentService(TaskAssignmentRepository assignmentRepository,
                                 TaskRepository taskRepository,
                                 StaffMemberRepository staffMemberRepository) {
        this.assignmentRepository = assignmentRepository;
        this.taskRepository = taskRepository;
        this.staffMemberRepository = staffMemberRepository;
    }

    // Create Assignment
    public TaskAssignmentResponseDto create(TaskAssignmentCreateDto dto) {
        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(TaskNotFoundException::new);

        StaffMember staff = staffMemberRepository.findById(dto.getStaffId())
                .orElseThrow(StaffMemberNotFoundException::new);


        TaskAssignment assignment = new TaskAssignment();
        assignment.setTask(task);
        assignment.setAssignedStaff(staff);
        assignment.setTimeRange(dto.getTimeRange());
        assignment.setCompleted(false);

        TaskAssignment saved = assignmentRepository.save(assignment);
        return TaskAssignmentMapper.toDto(saved);
    }

    // Update Assignment
    public TaskAssignmentResponseDto update(Long id, TaskAssignmentUpdateDto dto) {
        TaskAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(AssignmentNotFoundException::new);

        if (dto.getTimeRange() != null)
            assignment.setTimeRange(dto.getTimeRange());
        if (dto.getCompleted() != null)
            assignment.setCompleted(dto.getCompleted());
        if (dto.getStaffId() != null) {
            StaffMember staff = staffMemberRepository.findById(dto.getStaffId())
                    .orElseThrow(StaffMemberNotFoundException::new);
            assignment.setAssignedStaff(staff);
        }

        TaskAssignment saved = assignmentRepository.save(assignment);
        return TaskAssignmentMapper.toDto(saved);
    }

    // Get single assignment
    public TaskAssignmentResponseDto get(Long id) {
        TaskAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(AssignmentNotFoundException::new);

        return TaskAssignmentMapper.toDto(assignment);
    }

    // Get all assignments for Task
    public List<TaskAssignmentResponseDto> getByTask(Long taskId) {
        return assignmentRepository.findByTask_Id(taskId).stream()
                .map(TaskAssignmentMapper::toDto)
                .collect(Collectors.toList());
    }

    // Get all assignments for Staff
    public List<TaskAssignmentResponseDto> getByStaff(Long staffId) {
        return assignmentRepository.findByAssignedStaff_Id(staffId).stream()
                .map(TaskAssignmentMapper::toDto)
                .collect(Collectors.toList());
    }

    // Get all assignments
    public List<TaskAssignmentResponseDto> getAll() {
        return assignmentRepository.findAll().stream()
                .map(TaskAssignmentMapper::toDto)
                .collect(Collectors.toList());
    }

    // Delete Assignment
    public void delete(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new AssignmentNotFoundException();
        }
        assignmentRepository.deleteById(id);
    }
}

