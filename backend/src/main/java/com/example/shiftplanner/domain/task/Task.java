package com.example.shiftplanner.domain.task;

import com.example.shiftplanner.domain.staff.QualificationLevel;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter
@Setter
@ToString

/**
 * Entity-Klasse für eine Aufgabe im Schichtplaner, die einem Mitarbeiter (staff) zugewiesen werden kann,
 * eine Mindestqualifikation hat, und neben Titel und Beschreibung auch einen Zeitbereich besitzt.
 */

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 2000, nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QualificationLevel qualificationLevel;

    @Embedded
    private TimeRange timeRange;

    @Column(nullable = false)
    private Boolean completed;

    @Column(updatable = false)
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    public Task() {}

    public Task(String name, String description, QualificationLevel qualificationLevel) {
        this.name = name;
        this.description = description;
        this.qualificationLevel = qualificationLevel;
        this.completed = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}