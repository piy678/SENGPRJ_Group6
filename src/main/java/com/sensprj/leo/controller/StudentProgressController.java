package com.sensprj.leo.controller;

import com.sensprj.leo.dto.*;
import com.sensprj.leo.entity.*;
import com.sensprj.leo.entity.enums.AssessmentStatus;
import com.sensprj.leo.repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.sensprj.leo.entity.enums.AssessmentStatus.REACHED;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentProgressController {

    private final UserRepository userRepository;
    private final AssessmentRepository assessmentRepository;
    private final LeoDependencyRepository leoDependencyRepository;
    private final CourseEnrollmentRepository courseEnrollmentRepository;

    @GetMapping("/{studentId}/courses")
    public ResponseEntity<List<CourseDto>> getCourses(@PathVariable Long studentId) {

        if (!userRepository.existsById(studentId)) {
            return ResponseEntity.notFound().build();
        }

        List<CourseDto> courses = courseEnrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(enr -> CourseDto.fromEntity(enr.getCourse()))
                // dedupe by course id
                .collect(java.util.stream.Collectors.toMap(
                        CourseDto::getId,
                        dto -> dto,
                        (a, b) -> a
                ))
                .values()
                .stream()
                .toList();

        return ResponseEntity.ok(courses);
    }


    @GetMapping("/{studentId}/progress")
    public ResponseEntity<StudentProgressDto> getProgress(@PathVariable Long studentId,
                                                          @RequestParam(required = false) Long courseId) {
        User student = userRepository.findById(studentId).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        List<Assessment> assessments = (courseId == null)
                ? assessmentRepository.findByStudent(student)
                : assessmentRepository.findActiveAssessmentsByStudentAndCourse(studentId, courseId);


        int achieved = 0;
        int partially = 0;
        int notAchieved = 0;
        int unmarked = 0;



        java.util.Map<Long, Assessment> byLeoId = new java.util.HashMap<>();
        for (Assessment a : assessments) {
            if (a.getLeo() != null) {
                byLeoId.put(a.getLeo().getId(), a);
            }
        }

        List<LeoRowDto> leoRows = new ArrayList<>();

        for (Assessment a : assessments) {
            Leo leo = a.getLeo();
            if (leo == null) continue;


            AssessmentStatus status = parseStatus(a.getStatus());

            switch (status) {
                case REACHED -> achieved++;
                case PARTIALLY_REACHED -> partially++;
                case NOT_REACHED -> notAchieved++;
                case UNMARKED -> unmarked++;
            }


            List<LeoDependency> prereqs = leoDependencyRepository.findByDependentLeo(leo);
            String dependsOn = prereqs.isEmpty()
                    ? "No dependencies"
                    : prereqs.stream()
                    .map(d -> d.getPrerequisiteLeo().getName())
                    .distinct()
                    .reduce((x, y) -> x + ", " + y)
                    .orElse("No dependencies");

            LeoRowDto row = new LeoRowDto();
            row.setLeoId(leo.getId());
            row.setTitle(leo.getName());
            row.setDependsOn(dependsOn);
            row.setStatus(mapStatusLabel(status));

            LocalDateTime last = a.getUpdatedAt() != null ? a.getUpdatedAt() : a.getAssessedAt();
            row.setLastUpdated(last);

            leoRows.add(row);
        }

        int total = achieved + partially + notAchieved + unmarked;
        int graded = achieved + partially + notAchieved;
        double progress;
        if (total == 0) {
            progress = 0.0;
        } else {
            double raw = (graded * 100.0) / total;
            progress = Math.round(raw * 100.0) / 100.0;
        }


        List<SuggestionDto> suggestionDtos = new ArrayList<>();
        List<BlockedDto> blockedDtos = new ArrayList<>();
        java.util.Map<Long, Leo> allLeoMap = new java.util.HashMap<>();


        for (Assessment a : assessments) {
            if (a.getLeo() != null) {
                allLeoMap.put(a.getLeo().getId(), a.getLeo());
            }
        }


        for (Leo baseLeo : new ArrayList<>(allLeoMap.values())) {

            for (LeoDependency d : leoDependencyRepository.findByDependentLeo(baseLeo)) {
                if (d.getPrerequisiteLeo() != null) {
                    allLeoMap.put(d.getPrerequisiteLeo().getId(), d.getPrerequisiteLeo());
                }
            }
            for (LeoDependency d : leoDependencyRepository.findByPrerequisiteLeo(baseLeo)) {
                if (d.getDependentLeo() != null) {
                    allLeoMap.put(d.getDependentLeo().getId(), d.getDependentLeo());
                }
            }
        }

        List<Leo> allLeos = new ArrayList<>(allLeoMap.values());

        for (Leo leo : allLeos) {


            Assessment existing = byLeoId.get(leo.getId());
            if (existing != null && parseStatus(existing.getStatus()) == AssessmentStatus.REACHED) {
                continue;
            }

            List<LeoDependency> prereqs = leoDependencyRepository.findByDependentLeo(leo);


            if (prereqs.isEmpty()) {
                SuggestionDto s = new SuggestionDto();
                s.setLeoTitle(leo.getName());
                s.setHasDependencies(false);
                s.setReady(true);
                s.setRationale("No prerequisites. You can start anytime.");
                suggestionDtos.add(s);
                continue;
            }


            List<String> missing = new ArrayList<>();

            for (LeoDependency dep : prereqs) {
                Leo prereqLeo = dep.getPrerequisiteLeo();
                Assessment prereqAss = byLeoId.get(prereqLeo.getId());

                boolean reached = prereqAss != null
                        && parseStatus(prereqAss.getStatus()) == AssessmentStatus.REACHED;

                if (!reached) {
                    missing.add(prereqLeo.getName());
                }
            }

            if (missing.isEmpty()) {

                Leo prereqLeo = prereqs.get(prereqs.size() - 1).getPrerequisiteLeo();
                Assessment prereqAss = byLeoId.get(prereqLeo.getId());
                LocalDateTime done = prereqAss.getUpdatedAt() != null
                        ? prereqAss.getUpdatedAt()
                        : prereqAss.getAssessedAt();

                SuggestionDto s = new SuggestionDto();
                s.setLeoTitle(leo.getName());
                s.setHasDependencies(true);
                s.setReady(true);
                s.setPrerequisiteTitle(prereqLeo.getName());
                s.setPrerequisiteCompletedOn(done);
                s.setRationale("Prerequisite '" + prereqLeo.getName() + "' completed on " + done.toLocalDate());

                suggestionDtos.add(s);
            } else {

                BlockedDto b = new BlockedDto();
                b.setLeoTitle(leo.getName());
                b.setMissingPrerequisites(missing);
                b.setText("This LEO requires prerequisites: " + String.join(", ", missing));
                b.setTip("Complete prerequisites first");
                blockedDtos.add(b);
            }
        }


        StudentProgressDto dto = new StudentProgressDto();
        dto.setTotalLeos(total);
        dto.setAchieved(achieved);
        dto.setPartially(partially);
        dto.setNotAchieved(notAchieved);
        dto.setLeoStatuses(leoRows);
        dto.setBlocked(blockedDtos);
        dto.setSuggestions(suggestionDtos);
        dto.setUnmarked(unmarked);
        dto.setProgress(progress);


        return ResponseEntity.ok(dto);
    }

    private AssessmentStatus parseStatus(String raw) {
        if (raw == null || raw.isBlank()) return AssessmentStatus.UNMARKED;

        String v = raw.trim().toUpperCase();

        try {
            return AssessmentStatus.valueOf(v);
        } catch (IllegalArgumentException ex) {

            return switch (v) {
                case "ACHIEVED" -> AssessmentStatus.REACHED;
                case "PARTIALLY" -> AssessmentStatus.PARTIALLY_REACHED;
                case "NOT ACHIEVED", "NOT_ACHIEVED" -> AssessmentStatus.NOT_REACHED;
                default -> AssessmentStatus.UNMARKED;
            };
        }
    }



    private String mapStatusLabel(AssessmentStatus status) {
        return switch (status) {
            case REACHED -> "Achieved";
            case PARTIALLY_REACHED -> "Partially";
            case NOT_REACHED -> "Not achieved";
            case UNMARKED -> "Unmarked";

        };
    }



}
