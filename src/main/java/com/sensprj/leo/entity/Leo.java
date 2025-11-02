package com.sensprj.leo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "leos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Leo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String topic;

    @Column(columnDefinition = "boolean default true")
    private Boolean isActive;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "leo_prerequisites",
            joinColumns = @JoinColumn(name = "leo_id"),
            inverseJoinColumns = @JoinColumn(name = "prerequisite_leo_id")
    )
    @ToString.Exclude
    private Set<Leo> prerequisites = new HashSet<>();

    @OneToMany(mappedBy = "leo", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Assessment> assessments = new HashSet<>();
}