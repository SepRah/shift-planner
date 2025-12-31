package com.example.shiftplanner.application.staff;

import com.example.shiftplanner.exception.StaffMemberNotFoundException;
import com.example.shiftplanner.infrastructure.StaffMemberRepository;
import com.example.shiftplanner.api.staff.dto.StaffMemberCreateDTO;
import com.example.shiftplanner.api.staff.dto.StaffMemberResponseDTO;
import com.example.shiftplanner.api.staff.dto.StaffMemberUpdateDTO;
import com.example.shiftplanner.domain.staff.Name;
import com.example.shiftplanner.domain.staff.QualificationLevel;
import com.example.shiftplanner.domain.staff.StaffMember;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StaffMemberServiceTest {

    @Mock
    private StaffMemberRepository staffMemberRepository;

    @InjectMocks
    private StaffMemberService staffMemberService;

    @Test
    void create_shouldSaveAndReturnResponseDto_whenNotDuplicate() {
        // given
        StaffMemberCreateDTO dto = new StaffMemberCreateDTO(
                "Henry",
                "Duck",
                QualificationLevel.JUNIOR,
                0.8
        );
        // Duplikatprüfung mocken
        when(staffMemberRepository.existsByNameFirstNameAndNameLastName("Henry", "Duck"))
                .thenReturn(false);

        // Repository soll das Entity "mit ID" zurückgeben
        StaffMember saved = new StaffMember(new Name("Henry", "Duck"), QualificationLevel.JUNIOR, 0.8);
        when(staffMemberRepository.save(any(StaffMember.class))).thenReturn(saved);

        // act
        StaffMemberResponseDTO result = staffMemberService.create(dto);

        // assert
        assertNotNull(result);
        assertEquals("Henry", result.firstName());
        assertEquals("Duck", result.lastName());
        assertEquals(QualificationLevel.JUNIOR, result.staffQualificationLevel());
        assertEquals(0.8, result.fte());

        // verify: Duplicate-Check + save aufgerufen
        verify(staffMemberRepository).existsByNameFirstNameAndNameLastName("Henry", "Duck");

        ArgumentCaptor<StaffMember> captor = ArgumentCaptor.forClass(StaffMember.class);
        verify(staffMemberRepository).save(captor.capture());

        StaffMember toSave = captor.getValue();
        assertEquals("Henry", toSave.getName().getFirstName());
        assertEquals("Duck", toSave.getName().getLastName());
        assertEquals(QualificationLevel.JUNIOR, toSave.getStaffQualificationLevel());
        assertEquals(0.8, toSave.getFte());
    }

    @Test
    void updateStaffMember_shouldApplyFieldsAndSave_andReturnUpdatedDto() {
        // given
        Long id = 1L;
        StaffMember existing = new StaffMember(
                new Name("Henry", "Duck"),
                QualificationLevel.JUNIOR,
                0.8
        );

        StaffMemberUpdateDTO updateDTO = new StaffMemberUpdateDTO(
                "Pia",                  // new firstName
                "Harry",                    // new lastName
                QualificationLevel.SENIOR, // new level
                1.0                        // new fte
        );

        when(staffMemberRepository.findStaffMemberById(id))
                .thenReturn(Optional.of(existing));

        // save gibt das gespeicherte Entity zurück
        when(staffMemberRepository.save(any(StaffMember.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // act
        StaffMemberResponseDTO result = staffMemberService.updateStaffMember(id, updateDTO);

        // assert: DTO enthält neue Werte
        assertNotNull(result);
        assertEquals("Pia", result.firstName());
        assertEquals("Harry", result.lastName());
        assertEquals(QualificationLevel.SENIOR, result.staffQualificationLevel());
        assertEquals(1.0, result.fte());

        // verify: find + save wurden aufgerufen
        verify(staffMemberRepository).findStaffMemberById(id);

        ArgumentCaptor<StaffMember> captor = ArgumentCaptor.forClass(StaffMember.class);
        verify(staffMemberRepository).save(captor.capture());

        StaffMember savedEntity = captor.getValue();

        // assert: Entity wurde korrekt aktualisiert
        assertEquals("Pia", savedEntity.getName().getFirstName());
        assertEquals("Harry", savedEntity.getName().getLastName());
        assertEquals(QualificationLevel.SENIOR, savedEntity.getStaffQualificationLevel());
        assertEquals(1.0, savedEntity.getFte());
    }

    @Test
    void updateStaffMember_shouldKeepOldValues_whenDtoFieldsAreNull() {
        // given
        Long id = 1L;
        StaffMember existing = new StaffMember(
                new Name("Henry", "Duck"),
                QualificationLevel.JUNIOR,
                0.8
        );

        // Nur FTE ändern, Rest null -> bleibt gleich
        StaffMemberUpdateDTO updateDTO = new StaffMemberUpdateDTO(
                null,
                null,
                null,
                1.0
        );

        when(staffMemberRepository.findStaffMemberById(id))
                .thenReturn(Optional.of(existing));

        when(staffMemberRepository.save(any(StaffMember.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // act
        StaffMemberResponseDTO result = staffMemberService.updateStaffMember(id, updateDTO);

        // assert
        assertEquals("Henry", result.firstName());
        assertEquals("Duck", result.lastName());
        assertEquals(QualificationLevel.JUNIOR, result.staffQualificationLevel());
        assertEquals(1.0, result.fte());

        verify(staffMemberRepository).save(any(StaffMember.class));
    }

    @Test
    void deleteStaffMember_shouldDelete_whenExists() {
        // given
        Long id = 4L;
        when(staffMemberRepository.existsStaffMemberById(id)).thenReturn(true);

        // act
        staffMemberService.deleteStaffMember(id);

        // assert
        verify(staffMemberRepository).existsStaffMemberById(id);
        verify(staffMemberRepository).deleteById(id);
        verifyNoMoreInteractions(staffMemberRepository);
    }

    @Test
    void deleteStaffMember_shouldThrowNotFound_whenNotExists() {
        // given
        Long id = 999L;
        when(staffMemberRepository.existsStaffMemberById(id)).thenReturn(false);

        // act + assert
        assertThrows(StaffMemberNotFoundException.class, () -> staffMemberService.deleteStaffMember(id));

        // ensure delete not called
        verify(staffMemberRepository).existsStaffMemberById(id);
        verify(staffMemberRepository, never()).deleteById(anyLong());
    }

    @Test
    void getStaffMemberById_shouldReturnDto_whenFound() {
        // given
        Long id = 4L;
        StaffMember entity = new StaffMember(
                new Name("Henry", "Duck"),
                QualificationLevel.SENIOR,
                1.0);

        when(staffMemberRepository.findStaffMemberById(id)).thenReturn(Optional.of(entity));

        // act
        StaffMemberResponseDTO result = staffMemberService.getStaffMemberById(id);

        // assert
        assertNotNull(result);
        assertEquals("Henry", result.firstName());
        assertEquals("Duck", result.lastName());
        assertEquals(QualificationLevel.SENIOR, result.staffQualificationLevel());
        assertEquals(1.0, result.fte());

        verify(staffMemberRepository).findStaffMemberById(id);
        verifyNoMoreInteractions(staffMemberRepository);
    }

    @Test
    void getStaffMemberById_shouldThrowNotFound_whenMissing() {
        // given
        Long id = 404L;
        when(staffMemberRepository.findStaffMemberById(id)).thenReturn(Optional.empty());

        // act + assert
        assertThrows(StaffMemberNotFoundException.class, () -> staffMemberService.getStaffMemberById(id));

        verify(staffMemberRepository).findStaffMemberById(id);
        verifyNoMoreInteractions(staffMemberRepository);
    }

    @Test
    void getStaffMemberByFullName_shouldReturnDto_whenFound() {
        // given
        String first = "Henry";
        String last = "Duck";
        StaffMember entity = new StaffMember(
                new Name(first, last),
                QualificationLevel.JUNIOR,
                0.6);

        when(staffMemberRepository.findByNameFirstNameAndNameLastName(first, last))
                .thenReturn(Optional.of(entity));

        // act
        StaffMemberResponseDTO result = staffMemberService.getStaffMemberByFullName(first, last);

        // assert
        assertNotNull(result);
        assertEquals(first, result.firstName());
        assertEquals(last, result.lastName());
        assertEquals(QualificationLevel.JUNIOR, result.staffQualificationLevel());
        assertEquals(0.6, result.fte());

        verify(staffMemberRepository).findByNameFirstNameAndNameLastName(first, last);
        verifyNoMoreInteractions(staffMemberRepository);
    }

    @Test
    void getStaffMemberByFullName_shouldThrowNotFound_whenMissing() {
        // given
        String first = "Nobody";
        String last = "Here";
        when(staffMemberRepository.findByNameFirstNameAndNameLastName(first, last))
                .thenReturn(Optional.empty());

        // act + assert
        assertThrows(StaffMemberNotFoundException.class,
                () -> staffMemberService.getStaffMemberByFullName(first, last));

        verify(staffMemberRepository).findByNameFirstNameAndNameLastName(first, last);
        verifyNoMoreInteractions(staffMemberRepository);
    }

    @Test
    void getAll_shouldReturnMappedDtos() {
        // given
        List<StaffMember> entities = List.of(
                new StaffMember(new Name("Anna", "Meier"), QualificationLevel.JUNIOR, 0.8),
                new StaffMember(new Name("Ben", "Muster"), QualificationLevel.SENIOR, 1.0)
        );

        when(staffMemberRepository.findAll()).thenReturn(entities);

        // act
        List<StaffMemberResponseDTO> result = staffMemberService.getAll();

        // assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Anna", result.getFirst().firstName());
        assertEquals("Meier", result.getFirst().lastName());
        assertEquals(QualificationLevel.JUNIOR, result.get(0).staffQualificationLevel());
        assertEquals(0.8, result.get(0).fte());

        assertEquals("Ben", result.get(1).firstName());
        assertEquals("Muster", result.get(1).lastName());
        assertEquals(QualificationLevel.SENIOR, result.get(1).staffQualificationLevel());
        assertEquals(1.0, result.get(1).fte());

        verify(staffMemberRepository).findAll();
        verifyNoMoreInteractions(staffMemberRepository);
    }

    @Test
    void getAll_shouldReturnEmptyList_whenNoneExist() {
        // given
        when(staffMemberRepository.findAll()).thenReturn(List.of());

        // act
        List<StaffMemberResponseDTO> result = staffMemberService.getAll();

        // assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(staffMemberRepository).findAll();
        verifyNoMoreInteractions(staffMemberRepository);
    }
}