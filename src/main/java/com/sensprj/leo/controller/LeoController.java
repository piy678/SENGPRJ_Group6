package com.sensprj.leo.controller;

import com.sensprj.leo.entity.Course;
import com.sensprj.leo.entity.Leo;
import com.sensprj.leo.entity.LeoDependency;
import com.sensprj.leo.entity.User;
import com.sensprj.leo.entity.enums.DependencyType;
import com.sensprj.leo.repository.CourseRepository;
import com.sensprj.leo.repository.LeoDependencyRepository;
import com.sensprj.leo.repository.LeoRepository;
import com.sensprj.leo.repository.UserRepository;
import com.sensprj.leo.service.AssessmentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sensprj.leo.service.LeoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leos")
@RequiredArgsConstructor
public class LeoController {

    private final LeoRepository leoRepository;
    private final CourseRepository courseRepository;
    private final LeoDependencyRepository dependencyRepository;
    private final UserRepository userRepository;
    private final AssessmentService assessmentService;
    private final LeoService leoService;


    @GetMapping("/course/{courseId}")
    public List<LeoDto> getLeosForCourse(@PathVariable Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        return leoRepository.findByCourse(course).stream().map(leo -> {
            LeoDto dto = LeoDto.fromEntity(leo);
            dto.setPrerequisiteCount(dependencyRepository.countByDependentLeoId(leo.getId()));
            return dto;
        }).toList();
    }

    @GetMapping("/{leoId}/prerequisites")
    public List<LeoDto> getPrerequisites(@PathVariable Long leoId) {
        return dependencyRepository.findByDependentLeoId(leoId).stream()
                .map(d -> LeoDto.fromEntity(d.getPrerequisiteLeo()))
                .toList();
    }
    @GetMapping("/{leoId}/dependents")
    public List<LeoDto> getDependents(@PathVariable Long leoId) {
        List<LeoDependency> deps = dependencyRepository.findByPrerequisiteLeoId(leoId);
        return deps.stream()
                .map(d -> LeoDto.fromEntity(d.getDependentLeo()))
                .toList();
    }
    @DeleteMapping("/{leoId}")
    public ResponseEntity<?> deleteLeo(
            @PathVariable Long leoId,
            @RequestParam(defaultValue = "false") boolean force
    ) {
        leoService.deleteLeo(leoId, force);
        return ResponseEntity.noContent().build();
    }




    @GetMapping("/course/{courseId}/dependencies")
    public List<Map<String, Long>> getDependencies(@PathVariable Long courseId) {
        return dependencyRepository.findEdgesByCourse(courseId).stream().map(r -> {
            Long source = (Long) r[0];
            Long target = (Long) r[1];
            return Map.of("source", source, "target", target);
        }).toList();

    }

    @PostMapping("/course/{courseId}")
    public ResponseEntity<LeoDto> createLeo(@PathVariable Long courseId,
                                            @RequestBody CreateLeoRequest request) {

        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return ResponseEntity.badRequest().build();
        }
        User teacher = userRepository.findById(request.getCreatedById()).orElse(null);
        Leo leo = new Leo();
        leo.setCourse(course);
        leo.setName(request.getTitle());
        leo.setDescription(request.getDescription());
        leo.setTopic("TODO");
        leo.setCreatedBy(teacher);
        leo.setCreatedAt(LocalDateTime.now());
        leo.setIsActive(true);

        if (teacher == null) {
            return ResponseEntity.badRequest().build();
        }

        leo = leoRepository.save(leo);

        assessmentService.createDefaultNotReachedForLeo(leo, teacher);

        if (request.getPrerequisiteLeoId() != null) {
            Leo prereq = leoRepository.findById(request.getPrerequisiteLeoId()).orElse(null);
            if (prereq != null) {
                LeoDependency dep = new LeoDependency();
                dep.setDependentLeo(leo);
                dep.setPrerequisiteLeo(prereq);
                dep.setDependencyType(DependencyType.PREREQUISITE);
                dependencyRepository.save(dep);
            }
        }

        return ResponseEntity.ok(LeoDto.fromEntity(leo));
    }

    @PostMapping("/backfill-not-reached")
    public ResponseEntity<?> backfill(@RequestParam Long teacherId) {
        User teacher = userRepository.findById(teacherId).orElse(null);
        if (teacher == null) return ResponseEntity.badRequest().body("Teacher not found");

        int created = assessmentService.backfillNotReachedForAll(teacher);
        return ResponseEntity.ok(Map.of("created", created));
    }

    @GetMapping("/course/{courseId}/search")
    public List<LeoDto>  searchLeosForCourse(@PathVariable Long courseId,
                                         @RequestParam(required = false) String q) {

        Course course = courseRepository.findById(courseId).orElseThrow();

        List<Leo> leos;
        if (q == null || q.trim().isEmpty()) {
            leos = leoRepository.findByCourse(course);
        } else {
            String query = q.trim();
            // nur Name:
            leos = leoRepository.findByCourseAndNameContainingIgnoreCase(course, query);
        }

        return leos.stream().map(LeoDto::fromEntity).toList();
    }


    // ===== DTOs =====

    @Data
    public static class CreateLeoRequest {
        private String title;
        private String description;
        private Long prerequisiteLeoId;
        private Long createdById;
    }

    @Data
    public static class LeoDto {
        private Long id;
        private String name;
        private String description;
        private String topic;
        private Boolean isActive;
        private Long prerequisiteCount;

        public static LeoDto fromEntity(Leo leo) {
            LeoDto dto = new LeoDto();
            dto.id = leo.getId();
            dto.name = leo.getName();
            dto.description = leo.getDescription();
            dto.topic = leo.getTopic();
            dto.isActive = leo.getIsActive();
            return dto;
        }
    }

}
