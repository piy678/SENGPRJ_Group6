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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leo_id", nullable = false)
    private Leo leo;

    @Column(length = 255)
    private String suggestedReason;

    @Column
    private Integer priority;

    @Column
    private Integer prerequisitesMet;

    @Column
    private Integer totalPrerequisites;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDismissed;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // neue Felder, damit DataInitializer kompiliert
    @Column(precision = 5, scale = 2)
    private BigDecimal readinessScore;

    @Column
    private Long lastPrerequisiteReachedDaysAgo;
}
