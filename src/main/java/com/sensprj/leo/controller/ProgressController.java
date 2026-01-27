package com.sensprj.leo.controller;

import com.sensprj.leo.entity.Assessment;
import com.sensprj.leo.entity.Course;
import com.sensprj.leo.entity.CourseEnrollment;
import com.sensprj.leo.entity.User;
import com.sensprj.leo.entity.enums.AssessmentStatus;
import com.sensprj.leo.repository.AssessmentRepository;
import com.sensprj.leo.repository.CourseEnrollmentRepository;
import com.sensprj.leo.repository.CourseRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final CourseRepository courseRepository;
    private final CourseEnrollmentRepository enrollmentRepository;
    private final AssessmentRepository assessmentRepository;

    @GetMapping("/course/{courseId}")
    public List<StudentProgressDto> getProgressForCourse(@PathVariable Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();

        List<CourseEnrollment> enrollments = enrollmentRepository.findByCourse(course);
        List<Assessment> assessments = assessmentRepository.findByLeoCourse(course);

        Map<Long, StudentProgressDto> map = new HashMap<>();

        for (CourseEnrollment e : enrollments) {
            User s = e.getStudent();
            StudentProgressDto dto = new StudentProgressDto();
            dto.setStudentId(s.getId());
            dto.setName(s.getFirstName() + " " + s.getLastName());
            map.put(s.getId(), dto);
        }

        for (Assessment a : assessments) {
            StudentProgressDto dto = map.get(a.getStudent().getId());
            if (dto == null) continue;

            dto.total++;

            AssessmentStatus status;
            try {
                String raw = a.getStatus();
                status = (raw == null || raw.isBlank())
                        ? AssessmentStatus.UNMARKED
                        : AssessmentStatus.valueOf(raw.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                status = AssessmentStatus.UNMARKED;
            }

            switch (status) {
                case REACHED -> dto.achieved++;
                case PARTIALLY_REACHED -> dto.partially++;
                case NOT_REACHED -> dto.notAchieved++;
                case UNMARKED -> dto.unmarked++;
            }

        }

        map.values().forEach(dto -> {
            if (dto.total == 0) {
                dto.progress = 0;
                return;
            }
            double done = dto.achieved + 0.5 * dto.partially;
            dto.progress = (int) Math.round(done * 100.0 / dto.total);
        });

        return new ArrayList<>(map.values());
    }

    @Data
    public static class StudentProgressDto {
        private Long studentId;
        private String name;
        private int achieved;
        private int partially;
        private int unmarked;
        private int notAchieved;
        private int total;
        private int progress;
    }
}
