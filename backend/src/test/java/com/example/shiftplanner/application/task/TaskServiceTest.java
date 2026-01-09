package com.example.shiftplanner.application.task;

import com.example.shiftplanner.api.task.dto.TaskCreateDto;
import com.example.shiftplanner.api.task.dto.TaskResponseDto;
import com.example.shiftplanner.domain.task.Task;
import com.example.shiftplanner.infrastructure.TaskRepository;
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
 * Unit tests for {@link TaskService}.
 * Tests core business logic for creating and retrieving tasks.
 */
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        TaskCreateDto dto = new TaskCreateDto();
        dto.setName("Test");
        Task task = new Task();
        task.setId(1L);
        task.setName("Test");
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDto result = taskService.create(dto);

        assertEquals("Test", result.getName());
    }

    @Test
    void testGetTaskNotFound() {
        when(taskRepository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> taskService.get(123L));
    }
}
