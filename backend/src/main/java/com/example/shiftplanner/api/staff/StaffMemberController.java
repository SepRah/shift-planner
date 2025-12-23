package com.example.shiftplanner.api.staff;

import com.example.shiftplanner.api.staff.dto.StaffMemberCreateDTO;
import com.example.shiftplanner.api.staff.dto.StaffMemberResponseDTO;
import com.example.shiftplanner.api.staff.dto.StaffMemberUpdateDTO;
import com.example.shiftplanner.application.staff.StaffMemberService;
import com.example.shiftplanner.domain.staff.StaffMember;
import com.example.shiftplanner.domain.staff.Name;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAnyRole;

/**
 * REST controller providing CRUD operations for {@link StaffMember} resources.
 * <p>
 * Responsibilities of this controller:
 * <ul>
 *   <li>Define REST endpoints (URLs, HTTP methods)</li>
 *   <li>Receive request data via {@code @PathVariable}, {@code @RequestParam}, and {@code @RequestBody}</li>
 *   <li>Delegate business logic execution to {@link StaffMemberService}</li>
 *   <li>Return appropriate HTTP responses (status codes + DTOs)</li>
 * </ul>
 * <p>
 * This controller contains no business logic. All domain rules and validations
 * are handled by the application service or the domain model.
 */
@RestController
@RequestMapping("/api/staffmembers")
public class StaffMemberController {

    private final StaffMemberService staffMemberService;

    public StaffMemberController(StaffMemberService staffMemberService) {
        this.staffMemberService = staffMemberService;
    }

    /**
     * Creates a new {@link StaffMember}.
     * <p>
     * <strong>HTTP:</strong> {@code POST /api/staffmembers}
     * <br>
     * <strong>Request body:</strong> {@link StaffMemberCreateDTO}
     * <br>
     * <strong>Response:</strong>
     * <ul>
     *   <li>{@code 201 Created} – if the staff member was successfully created</li>
     *   <li>{@code 400 Bad Request} – if validation fails</li>
     * </ul>
     *
     * @param createDTO DTO containing the data for the new staff member
     * @return the created staff member as {@link StaffMemberResponseDTO}
     */
    @PreAuthorize("hasAnyRole('ADMIN','SYSTEM_ADMIN')")
    @PostMapping
    public ResponseEntity<StaffMemberResponseDTO> createStaffMember(
            @Valid @RequestBody StaffMemberCreateDTO createDTO) {

        StaffMemberResponseDTO created = staffMemberService.create(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<StaffMemberResponseDTO> getAll() {
        return staffMemberService.getAll();
    }

    /**
     * Retrieves a staff member by its ID.
     * <p>
     * <strong>HTTP:</strong> {@code GET /api/staffmembers/{id}}
     * <br>
     * <strong>Response:</strong>
     * <ul>
     *   <li>{@code 200 OK} – staff member found</li>
     *   <li>{@code 404 Not Found} – if no staff member exists for the given ID</li>
     * </ul>
     *
     * @param id ID of the staff member
     * @return DTO representation of the staff member
     */
    @GetMapping("/{id}")
    public ResponseEntity<StaffMemberResponseDTO> getById(@PathVariable Long id) {
        StaffMemberResponseDTO dto = staffMemberService.getStaffMemberById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Retrieves a staff member by full name (first and last name).
     * <p>
     * <strong>HTTP:</strong> {@code GET /api/staffmembers/search?firstName=...&lastName=...}
     * <br>
     * <strong>Example:</strong>
     * <pre>
     * GET /api/staffmembers/search?firstName=Anna&lastName=Meier
     * </pre>
     * <strong>Response:</strong>
     * <ul>
     *   <li>{@code 200 OK} – matching staff member found</li>
     *   <li>{@code 404 Not Found} – if no matching staff member exists</li>
     * </ul>
     *
     * @param firstName the staff member’s first name
     * @param lastName  the staff member’s last name
     * @return DTO representation of the staff member
     */
    @GetMapping("/search")
    public ResponseEntity<StaffMemberResponseDTO> getByFullName(
            @RequestParam String firstName,
            @RequestParam String lastName) {

        StaffMemberResponseDTO dto =
                staffMemberService.getStaffMemberByFullName(firstName, lastName);
        return ResponseEntity.ok(dto);
    }

    /**
     * Partially updates an existing {@link StaffMember}.
     * <p>
     * <strong>HTTP:</strong> {@code PATCH /api/staffmembers/{id}}
     * <br>
     * <strong>Request body:</strong> {@link StaffMemberUpdateDTO} (only fields to be changed)
     * <br>
     * <strong>Update behaviour:</strong>
     * <ul>
     *   <li>Only non-null fields in the update DTO are applied.</li>
     *   <li>Domain validation is enforced (e.g. FTE range, name validity).</li>
     *   <li>Value objects such as {@link Name} are replaced, not mutated.</li>
     * </ul>
     * <strong>Response:</strong>
     * <ul>
     *   <li>{@code 200 OK} – update successful</li>
     *   <li>{@code 404 Not Found} – if the staff member does not exist</li>
     * </ul>
     *
     * @param id        ID of the staff member to update
     * @param updateDTO contains the fields to update (partial update)
     * @return updated staff member as {@link StaffMemberResponseDTO}
     */
    @PreAuthorize("hasAnyRole('ADMIN','SYSTEM_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<StaffMemberResponseDTO> updateStaffMember(
            @PathVariable Long id,
            @RequestBody StaffMemberUpdateDTO updateDTO) {

        StaffMemberResponseDTO updated =
                staffMemberService.updateStaffMember(id, updateDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a staff member by its ID.
     * <p>
     * <strong>HTTP:</strong> {@code DELETE /api/staffmembers/{id}}
     * <br>
     * <strong>Response:</strong>
     * <ul>
     *   <li>{@code 204 No Content} – deletion successful</li>
     *   <li>{@code 404 Not Found} – if the staff member does not exist</li>
     * </ul>
     *
     * This endpoint returns no content by design.
     *
     * @param id ID of the staff member to delete
     */
    @PreAuthorize("hasAnyRole('ADMIN','SYSTEM_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStaffMember(@PathVariable Long id) {
        staffMemberService.deleteStaffMember(id);
    }
}

