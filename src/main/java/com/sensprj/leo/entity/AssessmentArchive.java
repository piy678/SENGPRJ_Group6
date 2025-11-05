package com.sensprj.leo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_archives")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentArchive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @Column(nullable = false, length = 50)
    private String oldStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime archivedAt;

    @Column(length = 255)
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archived_by", nullable = false)
    private User archivedBy;
}