package com.example.shiftplanner.api.staff;

import com.example.shiftplanner.domain.staff.QualificationLevel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qualification-levels")
public class QualificationLevelController {

    @GetMapping
    public QualificationLevel[] getAllLevels() {
        return QualificationLevel.values();
    }
}
