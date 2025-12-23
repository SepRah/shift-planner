package com.example.shiftplanner.application.staff;

import com.example.shiftplanner.infrastructure.StaffMemberRepository;
import com.example.shiftplanner.api.staff.dto.StaffMemberCreateDTO;
import com.example.shiftplanner.api.staff.dto.StaffMemberResponseDTO;
import com.example.shiftplanner.api.staff.dto.StaffMemberUpdateDTO;
import com.example.shiftplanner.domain.staff.Name;
import com.example.shiftplanner.domain.staff.QualificationLevel;
import com.example.shiftplanner.domain.staff.StaffMember;
import com.example.shiftplanner.exception.DuplicateStaffMemberException;
import com.example.shiftplanner.exception.StaffMemberNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
                "Test",
                "Enzmann",
                QualificationLevel.JUNIOR,
                0.8
        );
        // Duplikatprüfung mocken
        when(staffMemberRepository.existsByNameFirstNameAndNameLastName("Test", "Enzmann"))
                .thenReturn(false);

        // Repository soll das Entity "mit ID" zurückgeben
        StaffMember saved = new StaffMember(new Name("Test", "Enzmann"), QualificationLevel.JUNIOR, 0.8);
        when(staffMemberRepository.save(any(StaffMember.class))).thenReturn(saved);

        // act
        StaffMemberResponseDTO result = staffMemberService.create(dto);

        // assert
        assertNotNull(result);
        assertEquals("Test", result.firstName());
        assertEquals("Enzmann", result.lastName());
        assertEquals(QualificationLevel.JUNIOR, result.staffQualificationLevel());
        assertEquals(0.8, result.fte());

        // verify: Duplicate-Check + save aufgerufen
        verify(staffMemberRepository).existsByNameFirstNameAndNameLastName("Test", "Enzmann");

        ArgumentCaptor<StaffMember> captor = ArgumentCaptor.forClass(StaffMember.class);
        verify(staffMemberRepository).save(captor.capture());

        StaffMember toSave = captor.getValue();
        assertEquals("Test", toSave.getName().getFirstName());
        assertEquals("Enzmann", toSave.getName().getLastName());
        assertEquals(QualificationLevel.JUNIOR, toSave.getStaffQualificationLevel());
        assertEquals(0.8, toSave.getFte());
    }

    @Test
    void updateStaffMember() {
    }

    @Test
    void deleteStaffMember() {
    }

    @Test
    void getStaffMemberById() {
    }

    @Test
    void getStaffMemberByFullName() {
    }
}