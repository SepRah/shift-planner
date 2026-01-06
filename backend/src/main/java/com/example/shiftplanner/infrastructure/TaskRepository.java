package com.example.shiftplanner.infrastructure;

import com.example.shiftplanner.domain.task.Task;
import com.example.shiftplanner.domain.staff.QualificationLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Spring Data JPA repository for {@link Task} entities.
 * <p>
 * Provides CRUD operations and additional query methods based on
 * Spring Data's method name conventions.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Persisting and loading {@link Task} instances.</li>
 *   <li>Querying tasks by qualification level, name, and active status.</li>
 * </ul>
 * @author Benjamin Traffelet
 * @version 1.0
 * @since 2025-12-20
 */

public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Finds all tasks that are marked as active.
     * @return list of active tasks
     */
    List<Task> findByActiveTrue();
}
