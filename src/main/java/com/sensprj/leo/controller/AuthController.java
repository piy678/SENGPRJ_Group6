package com.sensprj.leo.controller;

import com.sensprj.leo.entity.User;
import com.sensprj.leo.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/login")

    public ResponseEntity<UserDto> login(@RequestBody LoginRequest request) {

        System.out.println("Login attempt: " + request.getUsername());

        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (user.getPasswordHash() == null || !user.getPasswordHash().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(UserDto.fromEntity(user));
    }


    @GetMapping("/debug/users")
    public List<String> debugUsers() {
        return userRepository.findAll().stream().map(User::getUsername).toList();
    }


    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class UserDto {
        private Long id;
        private String username;
        private String role;
        private String firstName;
        private String lastName;

        public static UserDto fromEntity(User user) {
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setRole(user.getRole());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            return dto;
        }
    }
}
