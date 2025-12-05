package com.example.shiftplanner.infrastructure;


import com.example.shiftplanner.domain.task.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Long> {

    List<TaskAssignment> findByAssignedStaff_Id(Long staffId);

    List<TaskAssignment> findByTask_Id(Long taskId);
}

