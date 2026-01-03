package com.sensprj.leo.controller;

import com.sensprj.leo.entity.Course;
import com.sensprj.leo.entity.User;
import com.sensprj.leo.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.sensprj.leo.entity.CourseEnrollment;
import com.sensprj.leo.repository.CourseEnrollmentRepository;
import java.time.LocalDateTime;
import com.sensprj.leo.repository.CourseRepository;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserRepository userRepository;
    private final CourseEnrollmentRepository enrollmentRepository;

    @GetMapping
    public List<UserDto> listUsers(@RequestParam(required = false) String role) {
        List<User> users = (role == null || role.isBlank())
                ? userRepository.findAll()
                : userRepository.findByRole(role);

        return users.stream().map(UserDto::fromEntity).toList();
    }

    @Data
    public static class CreateCourseRequest {
        private Long teacherId;
        private String name;
        private List<Long> studentIds; // NEU
    }

    @Data
    public static class UserDto {
        private Long id;
        private String username;
        private String role;
        private String firstName;
        private String lastName;

        public static UserDto fromEntity(User u) {
            UserDto dto = new UserDto();
            dto.id = u.getId();
            dto.username = u.getUsername();
            dto.role = u.getRole();
            dto.firstName = u.getFirstName();
            dto.lastName = u.getLastName();
            return dto;
        }
    }
}
