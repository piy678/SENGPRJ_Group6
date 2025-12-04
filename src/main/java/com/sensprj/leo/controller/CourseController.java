package com.sensprj.leo.controller;

import com.sensprj.leo.entity.Course;
import com.sensprj.leo.entity.User;
import com.sensprj.leo.repository.CourseRepository;
import com.sensprj.leo.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CourseController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;


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
        if (teacher == null) {
            return ResponseEntity.badRequest().build();
        }

        Course course = new Course();
        course.setName(request.getName());
        course.setTeacher(teacher);
        course.setIsActive(true);

        course = courseRepository.save(course);
        return ResponseEntity.ok(CourseDto.fromEntity(course));
    }


    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            return ResponseEntity.notFound().build();
        }
        courseRepository.deleteById(courseId);
        return ResponseEntity.noContent().build();
    }

    // ===== DTOs =====

    @Data
    public static class CreateCourseRequest {
        private Long teacherId;
        private String name;
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
