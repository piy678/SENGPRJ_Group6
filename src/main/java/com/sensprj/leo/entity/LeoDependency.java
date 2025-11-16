package com.sensprj.leo.entity;

import jakarta.persistence.*;
import lombok.*;
import com.sensprj.leo.entity.enums.DependencyType;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "leo_dependencies",
        indexes = {
                @Index(name = "idx_leo_dep_prerequisite", columnList = "prerequisite_leo_id"),
                @Index(name = "idx_leo_dep_dependent", columnList = "dependent_leo_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"dependent_leo_id", "prerequisite_leo_id"},
                        name = "uk_leo_dependency"
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeoDependency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependent_leo_id", nullable = false)
    private Leo dependentLeo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prerequisite_leo_id", nullable = false)
    private Leo prerequisiteLeo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DependencyType dependencyType = DependencyType.PREREQUISITE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
