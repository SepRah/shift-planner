package com.example.shiftplanner.infrastructure;

import com.example.shiftplanner.domain.task.Task;
import com.example.shiftplanner.domain.staff.QualificationLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository for Task entity.
 * Handles basic CRUD and task-specific queries.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByQualificationLevel(QualificationLevel level);
    List<Task> findByName(String name);

}
