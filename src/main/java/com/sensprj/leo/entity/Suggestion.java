package com.sensprj.leo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "suggestions",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"student_id", "leo_id"},
                        name = "uk_student_leo_suggestion"
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suggestion {

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

    // Fachfelder

    @Column(name = "suggested_reason", length = 255)
    private String suggestedReason;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "prerequisites_met")
    private Integer prerequisitesMet;

    @Column(name = "total_prerequisites")
    private Integer totalPrerequisites;

    @Builder.Default
    @Column(name = "is_dismissed", columnDefinition = "boolean default false")
    private Boolean isDismissed = false;

    @Column(name = "readiness_score", precision = 5, scale = 2)
    private BigDecimal readinessScore;

    @Column(name = "last_prerequisite_reached_days_ago")
    private Long lastPrerequisiteReachedDaysAgo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Lifecycle Hooks

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.isDismissed == null) {
            this.isDismissed = false;
        }
    }
}
