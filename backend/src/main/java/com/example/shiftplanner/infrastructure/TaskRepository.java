package com.example.shiftplanner.infrastructure;

import com.example.shiftplanner.domain.task.Task;
import com.example.shiftplanner.domain.staff.QualificationLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByTaskId(Long taskId);
    List<Task> findByAssignedStaffId(Long assignedStaffId);
    List<Task> findByCompletedFalse();
    List<Task> findByQualificationLevel(QualificationLevel level);
    List<Task> findByName(String name);
    
}
