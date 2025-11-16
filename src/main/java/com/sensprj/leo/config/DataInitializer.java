package com.sensprj.leo.config;

import com.sensprj.leo.entity.*;
import com.sensprj.leo.entity.enums.AssessmentStatus;
import com.sensprj.leo.entity.enums.DependencyType;
import com.sensprj.leo.entity.enums.UserRole;
import com.sensprj.leo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final LeoRepository leoRepository;
    private final LeoDependencyRepository leoDependencyRepository;
    private final AssessmentRepository assessmentRepository;
    private final SuggestionRepository suggestionRepository;

    @Override
    public void run(String... args) {

        // Щоб не дублювати дані при кожному рестарті
        if (userRepository.count() > 0) {
            return;
        }

        // ======================
        // 1. TEACHER
        // ======================

        User teacher = new User();
        teacher.setEmail("teacher@example.com");
        teacher.setUsername("teacher1");
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setPasswordHash("password");   // тут пізніше буде bcrypt
        teacher.setRole(UserRole.TEACHER);
        teacher.setIsActive(true);
        teacher = userRepository.save(teacher);

        // ======================
        // 2. COURSE
        // ======================

        Course course = new Course();
        course.setName("Software Engineering – LEO Demo");
        course.setTeacher(teacher);
        course.setIsActive(true);
        course = courseRepository.save(course);

        // ======================
        // 3. STUDENT + ENROLLMENT
        // ======================

        User student = new User();
        student.setEmail("student@example.com");
        student.setUsername("student1");
        student.setFirstName("Alice");
        student.setLastName("Student");
        student.setPasswordHash("password");
        student.setRole(UserRole.STUDENT);
        student.setIsActive(true);
        student = userRepository.save(student);

        CourseEnrollment enrollment = new CourseEnrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment = courseEnrollmentRepository.save(enrollment);

        // ======================
        // 4. LEO (Learning Elements)
        // ======================

        Leo leoBasics = new Leo();
        leoBasics.setCourse(course);
        leoBasics.setName("Basics of Requirements Engineering");
        leoBasics.setDescription("Student understands the basics of functional and non-functional requirements.");
        leoBasics.setTopic("Requirements Engineering");
        leoBasics.setIsActive(true);
        leoBasics = leoRepository.save(leoBasics);

        Leo leoAdvanced = new Leo();
        leoAdvanced.setCourse(course);
        leoAdvanced.setName("Advanced Requirements Engineering");
        leoAdvanced.setDescription("Student can model and manage complex requirements.");
        leoAdvanced.setTopic("Requirements Engineering");
        leoAdvanced.setIsActive(true);
        leoAdvanced = leoRepository.save(leoAdvanced);

        // ======================
        // 5. LEO DEPENDENCY (Advanced depends on Basics)
        // ======================

        LeoDependency dependency = new LeoDependency();
        dependency.setDependentLeo(leoAdvanced);
        dependency.setPrerequisiteLeo(leoBasics);
        dependency.setDependencyType(DependencyType.PREREQUISITE);
        leoDependencyRepository.save(dependency);

        // ======================
        // 6. ASSESSMENT (Basics REACHED)
        // ======================

        Assessment assessment = new Assessment();
        assessment.setStudent(student);
        assessment.setLeo(leoBasics);
        assessment.setAssessedBy(teacher);
        assessment.setStatus(AssessmentStatus.REACHED);
        assessment.setNotes("Student has successfully reached the basic learning outcome.");
        assessment.setIsArchived(false);
        assessment.setAssessedAt(LocalDateTime.now());
        assessmentRepository.save(assessment);

        // ======================
        // 7. SUGGESTION (Suggest Advanced LEO)
        // ======================

        Suggestion suggestion = new Suggestion();
        suggestion.setStudent(student);
        suggestion.setLeo(leoAdvanced);
        suggestion.setSuggestedReason("ALL prerequisites for this LEO are reached. Student is ready for advanced content.");
        suggestion.setPriority(1);
        suggestion.setPrerequisitesMet(1);
        suggestion.setTotalPrerequisites(1);
        suggestion.setReadinessScore(BigDecimal.valueOf(95.00));
        suggestion.setLastPrerequisiteReachedDaysAgo(0L);
        suggestion.setIsDismissed(false);
        suggestionRepository.save(suggestion);
    }
}
