package com.sensprj.leo.controller;

import com.sensprj.leo.entity.*;
import com.sensprj.leo.entity.enums.AssessmentStatus;
import com.sensprj.leo.repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AssessmentController {

    private final CourseRepository courseRepository;
    private final CourseEnrollmentRepository enrollmentRepository;
    private final LeoRepository leoRepository;
    private final UserRepository userRepository;
    private final AssessmentRepository assessmentRepository;


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


    @PostMapping("/student/{studentId}")
    public ResponseEntity<Void> saveAssessments(
            @PathVariable Long studentId,
            @RequestBody SaveAssessmentsRequest request) {

        User student = userRepository.findById(studentId).orElse(null);
        User teacher = userRepository.findById(request.getTeacherId()).orElse(null);
        if (student == null || teacher == null) {
            return ResponseEntity.badRequest().build();
        }

        for (AssessmentEntry entry : request.getEntries()) {
            Leo leo = leoRepository.findById(entry.getLeoId()).orElse(null);
            if (leo == null) continue;

            Assessment a = new Assessment();
            a.setStudent(student);
            a.setLeo(leo);
            a.setAssessedBy(teacher);
            a.setStatus(entry.getStatus().name());
            a.setIsArchived(false);
            a.setAssessedAt(LocalDateTime.now());
            assessmentRepository.save(a);
        }

        return ResponseEntity.ok().build();
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
    }
}
