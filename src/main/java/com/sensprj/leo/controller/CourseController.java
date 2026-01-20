package com.sensprj.leo.controller;

import com.sensprj.leo.entity.Course;
import com.sensprj.leo.entity.CourseEnrollment;
import com.sensprj.leo.entity.User;
import com.sensprj.leo.repository.AssessmentRepository;
import com.sensprj.leo.repository.CourseEnrollmentRepository;
import com.sensprj.leo.repository.CourseRepository;
import com.sensprj.leo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final AssessmentRepository assessmentRepository;
    private final CourseEnrollmentRepository enrollmentRepository;


    @GetMapping("/teacher/{teacherId}")
    public List<CourseDto> getCoursesForTeacher(@PathVariable Long teacherId) {
        User teacher = userRepository.findById(teacherId).orElseThrow();
        return courseRepository.findByTeacher(teacher)
                .stream()
                .map(CourseDto::fromEntity)
                .toList();
    }
    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CreateCourseRequest request) {
        User teacher = userRepository.findById(request.getTeacherId()).orElse(null);
        if (teacher == null) return ResponseEntity.badRequest().build();

        Course course = new Course();
        course.setName(request.getName());
        course.setTeacher(teacher);
        course.setIsActive(true);

        course = courseRepository.save(course);

        // Schüler zuordnen
        if (request.getStudentIds() != null) {
            for (Long studentId : request.getStudentIds()) {
                User student = userRepository.findById(studentId).orElse(null);
                if (student == null) continue;

                if (enrollmentRepository
                        .findByCourseIdAndStudentId(course.getId(), studentId)
                        .isPresent()) continue;

                CourseEnrollment e = new CourseEnrollment();
                e.setCourse(course);
                e.setStudent(student);
                e.setEnrolledAt(LocalDateTime.now());
                enrollmentRepository.save(e);
            }
        }

        return ResponseEntity.ok(CourseDto.fromEntity(course));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable Long id) {
        Course c = courseRepository.findById(id).orElse(null);
        if (c == null) return ResponseEntity.notFound().build();

        CourseDto dto = new CourseDto();
        dto.setId(c.getId());
        dto.setName(c.getName());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/course/{courseId}/stats")
    public Map<Long, Long> getReachedStats(@PathVariable Long courseId) {
        List<Object[]> rows = assessmentRepository.countReachedByLeoInCourse(courseId);

        Map<Long, Long> map = new HashMap<>();
        for (Object[] r : rows) {
            Long leoId = (Long) r[0];
            Long cnt = (Long) r[1];
            map.put(leoId, cnt);
        }
        return map;
    }



//    @PostMapping
//    public ResponseEntity<CourseDto> createCourse(@RequestBody CreateCourseRequest request) {
//        User teacher = userRepository.findById(request.getTeacherId()).orElse(null);
//        if (teacher == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Course course = new Course();
//        course.setName(request.getName());
//        course.setTeacher(teacher);
//        course.setIsActive(true);
//
//        course = courseRepository.save(course);
//        return ResponseEntity.ok(CourseDto.fromEntity(course));
//    }


//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
//        if (!courseRepository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//
//        courseRepository.deleteById(id);
//        return ResponseEntity.noContent().build(); // 204
//    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (!courseRepository.existsById(id)) return ResponseEntity.notFound().build();

        enrollmentRepository.deleteByCourseId(id);
        courseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    // ===== DTOs =====

    @Data
    public static class CreateCourseRequest {
        private Long teacherId;
        private String name;
        private List<Long> studentIds;
    }


    @Data
    public static class CourseDto {
        private Long id;
        private String name;
        private Long teacherId;
        private String teacherName;

        public static CourseDto fromEntity(Course c) {
            CourseDto dto = new CourseDto();
            dto.id = c.getId();
            dto.name = c.getName();
            if (c.getTeacher() != null) {
                dto.teacherId = c.getTeacher().getId();
                dto.teacherName = c.getTeacher().getFirstName() + " " + c.getTeacher().getLastName();
            }
            return dto;
        }
    }
}
