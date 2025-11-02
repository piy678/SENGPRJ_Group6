package com.sensprj.leo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "leo_id"}, name = "uk_student_leo_assessment")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leo_id", nullable = false)
    private Leo leo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessed_by", nullable = false)
    private User assessedBy;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_assessment_id")
    private Assessment previousAssessment;

    @Column(columnDefinition = "boolean default false")
    private Boolean isArchived;

    private LocalDateTime assessedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL)
    @ToString.Exclude
    private java.util.Set<AssessmentArchive> archives = new java.util.HashSet<>();
}
