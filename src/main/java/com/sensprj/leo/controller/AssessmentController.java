package com.sensprj.leo.controller;

import com.sensprj.leo.entity.*;
import com.sensprj.leo.entity.enums.AssessmentStatus;
import com.sensprj.leo.repository.*;
import com.sensprj.leo.service.AssessmentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final CourseRepository courseRepository;
    private final CourseEnrollmentRepository enrollmentRepository;
    private final LeoRepository leoRepository;
    private final UserRepository userRepository;
    private final AssessmentRepository assessmentRepository;
    private final AssessmentService assessmentService;

    @GetMapping("/course/{courseId}/students")
    public List<StudentDto> getStudentsForCourse(@PathVariable Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        return enrollmentRepository.findByCourse(course).stream()
                .map(CourseEnrollment::getStudent)
                .map(StudentDto::fromEntity)
                .toList();
    }


    @GetMapping("/course/{courseId}/leos")
    public List<LeoSimpleDto> getLeosForCourse(@PathVariable Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        return leoRepository.findByCourse(course).stream()
                .map(LeoSimpleDto::fromEntity)
                .toList();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/student/{studentId}")
    public ResponseEntity<?> saveAssessments(
            @PathVariable Long studentId,
            @RequestBody SaveAssessmentsRequest request
    ) {
        User student = userRepository.findById(studentId).orElseThrow();
        User teacher = userRepository.findById(request.getTeacherId()).orElseThrow();

        List<Long> lockedLeoIds = new java.util.ArrayList<>();

        for (AssessmentEntry entry : request.getEntries()) {
            Leo leo = leoRepository.findById(entry.getLeoId()).orElseThrow();

            if (!assessmentService.isLeoUnlockedForStudent(student, leo)) {
                lockedLeoIds.add(leo.getId());
                continue;
            }

            LocalDateTime assessedAt = entry.getAssessedAt() != null
                    ? entry.getAssessedAt()
                    : LocalDateTime.now();

            assessmentService.upsertAssessment(student, leo, teacher, entry.getStatus(), assessedAt);
        }

        if (!lockedLeoIds.isEmpty()) {
            return ResponseEntity.status(409).body(Map.of(
                    "message", "Some LEOs are locked (prerequisites not reached).",
                    "lockedLeoIds", lockedLeoIds
            ));
        }

        return ResponseEntity.ok().build();
    }



    @GetMapping("/course/{courseId}/student/{studentId}/grade")
    public ResponseEntity<?> getGrade(@PathVariable Long courseId, @PathVariable Long studentId) {
        return ResponseEntity.ok(assessmentService.calculateGradeForStudent(courseId, studentId));
    }

    @GetMapping("/course/{courseId}/stats")
    public Map<Long, Long> getLeoReachedStats(@PathVariable Long courseId) {
        List<Object[]> rows = assessmentRepository.countReachedByLeoInCourse(courseId);

        Map<Long, Long> result = new HashMap<>();
        for (Object[] r : rows) {
            Long leoId = (Long) r[0];
            Long count = (Long) r[1];
            result.put(leoId, count);
        }
        return result;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/backfill/not-reached")
    public ResponseEntity<String> backfill(@RequestParam Long teacherId) {

        User teacher = userRepository.findById(teacherId).orElseThrow();

        int created = assessmentService.backfillNotReachedForAll(teacher);
        return ResponseEntity.ok("Created default UNMARKED assessments: " + created);
    }



    // ===== DTOs =====

    @Data
    public static class StudentDto {
        private Long id;
        private String name;
        public static StudentDto fromEntity(User u) {
            StudentDto dto = new StudentDto();
            dto.id = u.getId();
            dto.name = u.getFirstName() + " " + u.getLastName();
            return dto;
        }
    }

    @Data
    public static class LeoSimpleDto {
        private Long id;
        private String title;
        public static LeoSimpleDto fromEntity(Leo leo) {
            LeoSimpleDto dto = new LeoSimpleDto();
            dto.id = leo.getId();
            dto.title = leo.getName();
            return dto;
        }
    }

    @Data
    public static class SaveAssessmentsRequest {
        private Long teacherId;
        private List<AssessmentEntry> entries;
    }

    @Data
    public static class AssessmentEntry {
        private Long leoId;
        private AssessmentStatus status;
        private LocalDateTime assessedAt;
    }
}
