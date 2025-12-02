package com.sensprj.leo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "assessments",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"student_id", "leo_id"},
                        name = "uk_student_leo_assessment"
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Beziehungen

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leo_id", nullable = false)
    @ToString.Exclude
    private Leo leo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessed_by", nullable = false)
    @ToString.Exclude
    private User assessedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_assessment_id")
    @ToString.Exclude
    private Assessment previousAssessment;

    @Builder.Default
    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<AssessmentArchive> archives = new HashSet<>();

    // Fachfelder

    @Column(nullable = false, length = 50)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Builder.Default
    @Column(name = "is_archived", nullable = false, columnDefinition = "boolean default false")
    private Boolean isArchived = false;

    @Column(name = "assessed_at")
    private LocalDateTime assessedAt;

    // Audit-Felder

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Lifecycle Hooks

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        if (this.createdAt == null) {
            this.createdAt = now;
        }
        if (this.updatedAt == null) {
            this.updatedAt = now;
        }
        if (this.assessedAt == null) {
            this.assessedAt = now;
        }
        if (this.isArchived == null) {
            this.isArchived = false;
        }
        if (this.archives == null) {
            this.archives = new HashSet<>();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.isArchived == null) {
            this.isArchived = false;
        }
        if (this.archives == null) {
            this.archives = new HashSet<>();
        }
    }
}
