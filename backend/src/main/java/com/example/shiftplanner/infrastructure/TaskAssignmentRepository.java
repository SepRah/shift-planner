
/**
 * Spring Data JPA repository for {@link TaskAssignment} entities.
 * <p>
 * Provides CRUD operations and additional query methods based on
 * Spring Data's method name conventions.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Persisting and loading {@link TaskAssignment} instances.</li>
 *   <li>Querying assignments by staff, task, completion status, and time range.</li>
 * </ul>
 * @author Benjamin Traffelet
 * @version 1.0
 * @since 2025-12-20
 */

package com.example.shiftplanner.infrastructure;


import com.example.shiftplanner.domain.task.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Long> {

    /**
     * Finds all task assignments for a given staff member.
     * @param staffId the ID of the staff member
     * @return list of task assignments for the staff member
     */
    List<TaskAssignment> findByAssignedStaff_Id(Long staffId);

    /**
     * Finds all task assignments for a given task.
     * @param taskId the ID of the task
     * @return list of task assignments for the task
     */
    List<TaskAssignment> findByTask_Id(Long taskId);

}

