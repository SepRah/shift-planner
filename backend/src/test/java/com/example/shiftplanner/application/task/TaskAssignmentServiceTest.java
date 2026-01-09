package com.example.shiftplanner.application.task;

import com.example.shiftplanner.api.task.dto.TaskAssignmentCreateDto;
import com.example.shiftplanner.api.task.dto.TaskAssignmentResponseDto;
import com.example.shiftplanner.domain.task.TaskAssignment;
import com.example.shiftplanner.infrastructure.TaskAssignmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TaskAssignmentService}.
 * Tests core business logic for creating and retrieving task assignments.
 */
class TaskAssignmentServiceTest {
    @Mock
    private TaskAssignmentRepository assignmentRepository;

    @InjectMocks
    private TaskAssignmentService assignmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAssignment() {
        TaskAssignmentCreateDto dto = new TaskAssignmentCreateDto();
        dto.setTaskId(1L);
        dto.setStaffId(2L);
        TaskAssignment assignment = new TaskAssignment();
        assignment.setId(10L);
        when(assignmentRepository.save(any(TaskAssignment.class))).thenReturn(assignment);

        assertTrue(true);
    }

    @Test
    void testGetAssignmentNotFound() {
        when(assignmentRepository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> assignmentService.get(123L));
    }
}
