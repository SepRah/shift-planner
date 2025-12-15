package com.example.shiftplanner.infrastructure;

import com.example.shiftplanner.domain.staff.QualificationLevel;
import com.example.shiftplanner.domain.staff.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link StaffMember} entities.
 * <p>
 * Provides CRUD operations and additional query methods based on
 * Spring Data's method name conventions.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Persisting and loading {@link StaffMember} instances.</li>
 *   <li>Looking up staff members by ID, name and fte.</li>
 *   <li>Checking existence of staff members for validation use cases.</li>
 * </ul>
 */
@Repository
public interface StaffMemberRepository extends JpaRepository<StaffMember, Long> {
    /**
     * Finds a staff member by its ID.
     *
     * @param id the ID of the staff member
     * @return an {@link Optional} containing the staff member if found, or empty if not
     */
    Optional<StaffMember> findStaffMemberById(Long id);
    /**
     * Finds a staff member by full name (first and last name).
     *
     * @param firstName first name of the staff member
     * @param lastName  last name of the staff member
     * @return an {@link Optional} containing the staff member if found, or empty if not
     */
    Optional<StaffMember> findByNameFirstNameAndNameLastName (String firstName, String lastName);

    /**
     * Checks if a staff member with the given ID exists.
     *
     * @param id the ID to check
     * @return {@code true} if a staff member with this ID exists, {@code false} otherwise
     */
    boolean existsStaffMemberById(Long id);
    /**
     * Checks if a staff member with the given full name exists.
     *
     * @param firstName first name of the staff member
     * @param lastName  last name of the staff member
     * @return {@code true} if at least one staff member with this name exists
     */
    boolean existsByNameFirstNameAndNameLastName(String firstName, String lastName);


    /**
     * Returns all staff members with the given qualification value.
     * <p>
     *
     * @return list of all staff members
     */
    List<StaffMember> findAllByStaffQualificationLevel(QualificationLevel staffQualificationLevel);

    /**
     * Returns all staff members with the given fte value.
     * <p>
     *
     * @return list of all staff members
     */
    List<StaffMember> findAllByFte(double fte);
}
