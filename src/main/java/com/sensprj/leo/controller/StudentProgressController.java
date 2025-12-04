package com.sensprj.leo.controller;

import com.sensprj.leo.entity.*;
import com.sensprj.leo.entity.enums.AssessmentStatus;
import com.sensprj.leo.repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class StudentProgressController {

    private final UserRepository userRepository;
    private final AssessmentRepository assessmentRepository;
    private final SuggestionRepository suggestionRepository;
    private final LeoDependencyRepository leoDependencyRepository;

    @GetMapping("/{studentId}/progress")
    public ResponseEntity<StudentProgressDto> getProgress(@PathVariable Long studentId) {
        User student = userRepository.findById(studentId).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        List<Assessment> assessments = assessmentRepository.findByStudent(student);

        int achieved = 0;
        int partially = 0;
        int notAchieved = 0;
        int unrated = 0;

        List<LeoRowDto> leoRows = new ArrayList<>();

        for (Assessment a : assessments) {
            AssessmentStatus status = AssessmentStatus.valueOf(a.getStatus());

            switch (status) {
                case REACHED -> achieved++;
                case PARTIALLY_REACHED -> partially++;
                case NOT_REACHED -> notAchieved++;
                default -> unrated++;
            }

            Leo leo = a.getLeo();


            String dependsOn = leoDependencyRepository.findByDependentLeo(leo)
                    .stream()
                    .findFirst()
                    .map(dep -> dep.getPrerequisiteLeo().getName())
                    .orElse("No dependencies");

            LeoRowDto row = new LeoRowDto();
            row.setTitle(leo.getName());
            row.setDependsOn(dependsOn);
            row.setStatus(mapStatusLabel(status));
            leoRows.add(row);
        }

        int total = achieved + partially + notAchieved + unrated;


        List<Suggestion> suggestions = suggestionRepository.findByStudent(student);
        List<String> suggestionTexts = suggestions.stream()
                .map(Suggestion::getSuggestedReason)
                .toList();

        StudentProgressDto dto = new StudentProgressDto();
        dto.setTotalLeos(total);
        dto.setAchieved(achieved);
        dto.setPartially(partially);
        dto.setNotAchieved(notAchieved);
        dto.setUnrated(unrated);
        dto.setLeoStatuses(leoRows);
        dto.setSuggestions(suggestionTexts);

        return ResponseEntity.ok(dto);
    }

    private String mapStatusLabel(AssessmentStatus status) {
        return switch (status) {
            case REACHED -> "Achieved";
            case PARTIALLY_REACHED -> "Partially";
            case NOT_REACHED -> "Not achieved";
            default -> "Unrated";
        };
    }

    // ==== DTO-Klassen ====

    @Data
    public static class StudentProgressDto {
        private int totalLeos;
        private int achieved;
        private int partially;
        private int notAchieved;
        private int unrated;
        private List<LeoRowDto> leoStatuses;
        private List<String> suggestions;
    }

    @Data
    public static class LeoRowDto {
        private String title;
        private String dependsOn;
        private String status;
    }
}
