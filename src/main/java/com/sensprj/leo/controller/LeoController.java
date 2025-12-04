package com.sensprj.leo.controller;

import com.sensprj.leo.entity.Course;
import com.sensprj.leo.entity.Leo;
import com.sensprj.leo.entity.LeoDependency;
import com.sensprj.leo.entity.enums.DependencyType;
import com.sensprj.leo.repository.CourseRepository;
import com.sensprj.leo.repository.LeoDependencyRepository;
import com.sensprj.leo.repository.LeoRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class LeoController {

    private final LeoRepository leoRepository;
    private final CourseRepository courseRepository;
    private final LeoDependencyRepository dependencyRepository;


    @GetMapping("/course/{courseId}")
    public List<LeoDto> getLeosForCourse(@PathVariable Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        return leoRepository.findByCourse(course)
                .stream()
                .map(LeoDto::fromEntity)
                .toList();
    }


    @PostMapping("/course/{courseId}")
    public ResponseEntity<LeoDto> createLeo(@PathVariable Long courseId,
                                            @RequestBody CreateLeoRequest request) {

        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return ResponseEntity.badRequest().build();
        }

        Leo leo = new Leo();
        leo.setCourse(course);
        leo.setName(request.getTitle());
        leo.setDescription(request.getDescription());
        leo.setTopic("TODO");             // kannst du später dynamisch machen
        leo.setIsActive(true);
        leo = leoRepository.save(leo);


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

    // ===== DTOs =====

    @Data
    public static class CreateLeoRequest {
        private String title;
        private String description;
        private Long prerequisiteLeoId; // optional
    }

    @Data
    public static class LeoDto {
        private Long id;
        private String name;
        private String description;

        public static LeoDto fromEntity(Leo leo) {
            LeoDto dto = new LeoDto();
            dto.id = leo.getId();
            dto.name = leo.getName();
            dto.description = leo.getDescription();
            return dto;
        }
    }
}
