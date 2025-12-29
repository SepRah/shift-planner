package com.example.shiftplanner.infrastructure;


import com.example.shiftplanner.domain.task.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Long> {

    List<TaskAssignment> findByAssignedStaff_Id(Long staffId);

    List<TaskAssignment> findByTask_Id(Long taskId);

    List<TaskAssignment> findByCompletedFalse();

    List<TaskAssignment> findByTimeRange_StartBetween(Instant start, Instant end);

    List<TaskAssignment> findByAssignedStaff_IdAndTimeRange_StartBetween(Long staffId, Instant start, Instant end);

    // Prüfe, ob es ein Assignment für Task, Staff und exakten Zeitraum gibt
    List<TaskAssignment> findByTask_IdAndAssignedStaff_IdAndTimeRange_StartAndTimeRange_End(
        Long taskId, Long staffId, Instant start, Instant end
    );


}

