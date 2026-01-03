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

        if (userRepository.count() > 0) {
            return;
        }

        // =========================
        // 1. TEACHERS
        // =========================

        User teacher1 = new User();
        teacher1.setEmail("teacher@example.com");
        teacher1.setUsername("teacher1");
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");
        teacher1.setPasswordHash("password");
        teacher1.setRole(UserRole.TEACHER.name());
        teacher1.setIsActive(true);
        teacher1 = userRepository.save(teacher1);

        User teacher2 = new User();
        teacher2.setEmail("teacher2@example.com");
        teacher2.setUsername("teacher2");
        teacher2.setFirstName("Maria");
        teacher2.setLastName("Schneider");
        teacher2.setPasswordHash("password");
        teacher2.setRole(UserRole.TEACHER.name());
        teacher2.setIsActive(true);
        teacher2 = userRepository.save(teacher2);

        User teacher3 = new User();
        teacher3.setEmail("teacher3@example.com");
        teacher3.setUsername("teacher3");
        teacher3.setFirstName("Peter");
        teacher3.setLastName("Huber");
        teacher3.setPasswordHash("password");
        teacher3.setRole(UserRole.TEACHER.name());
        teacher3.setIsActive(true);
        teacher3 = userRepository.save(teacher3);

        // =========================
        // 2. COURSE
        // =========================

        Course course = new Course();
        course.setName("Software Engineering – LEO Demo");
        course.setTeacher(teacher1); // головний викладач курсу
        course.setIsActive(true);
        course = courseRepository.save(course);

        // =========================
        // 3. STUDENTS
        // =========================

        User student1 = new User();
        student1.setEmail("student@example.com");
        student1.setUsername("student1");
        student1.setFirstName("Alice");
        student1.setLastName("Student");
        student1.setPasswordHash("password");
        student1.setRole(UserRole.STUDENT.name());
        student1.setIsActive(true);
        student1 = userRepository.save(student1);

        User student2 = new User();
        student2.setEmail("student2@example.com");
        student2.setUsername("student2");
        student2.setFirstName("Bob");
        student2.setLastName("Miller");
        student2.setPasswordHash("password");
        student2.setRole(UserRole.STUDENT.name());
        student2.setIsActive(true);
        student2 = userRepository.save(student2);

        User student3 = new User();
        student3.setEmail("student3@example.com");
        student3.setUsername("student3");
        student3.setFirstName("Clara");
        student3.setLastName("Fischer");
        student3.setPasswordHash("password");
        student3.setRole(UserRole.STUDENT.name());
        student3.setIsActive(true);
        student3 = userRepository.save(student3);

        User student4 = new User();
        student4.setEmail("student4@example.com");
        student4.setUsername("student4");
        student4.setFirstName("David");
        student4.setLastName("Schmidt");
        student4.setPasswordHash("password");
        student4.setRole(UserRole.STUDENT.name());
        student4.setIsActive(true);
        student4 = userRepository.save(student4);

        User student5 = new User();
        student5.setEmail("student5@example.com");
        student5.setUsername("student5");
        student5.setFirstName("Emma");
        student5.setLastName("Maier");
        student5.setPasswordHash("password");
        student5.setRole(UserRole.STUDENT.name());
        student5.setIsActive(true);
        student5 = userRepository.save(student5);

        // =========================
        // 4. ENROLLMENTS
        // =========================

        CourseEnrollment enrollment1 = new CourseEnrollment();
        enrollment1.setCourse(course);
        enrollment1.setStudent(student1);
        enrollment1.setEnrolledAt(LocalDateTime.now());
        courseEnrollmentRepository.save(enrollment1);

        CourseEnrollment enrollment2 = new CourseEnrollment();
        enrollment2.setCourse(course);
        enrollment2.setStudent(student2);
        enrollment2.setEnrolledAt(LocalDateTime.now().minusDays(2));
        courseEnrollmentRepository.save(enrollment2);

        CourseEnrollment enrollment3 = new CourseEnrollment();
        enrollment3.setCourse(course);
        enrollment3.setStudent(student3);
        enrollment3.setEnrolledAt(LocalDateTime.now().minusDays(5));
        courseEnrollmentRepository.save(enrollment3);

        CourseEnrollment enrollment4 = new CourseEnrollment();
        enrollment4.setCourse(course);
        enrollment4.setStudent(student4);
        enrollment4.setEnrolledAt(LocalDateTime.now().minusDays(7));
        courseEnrollmentRepository.save(enrollment4);

        // student5

        // =========================
        // 5. LEOs
        // =========================

        Leo leoBasics = new Leo();
        leoBasics.setCourse(course);
        leoBasics.setName("Basics of Requirements Engineering");
        leoBasics.setDescription("Student understands the basics of functional and non-functional requirements.");
        leoBasics.setTopic("Requirements Engineering");
        leoBasics.setIsActive(true);
        User teacher = userRepository.findByUsername("teacher1").orElseThrow();
        leoBasics.setCreatedBy(teacher);

        leoBasics.setCreatedBy(teacher);
        leoBasics = leoRepository.save(leoBasics);

        Leo leoAdvanced = new Leo();
        leoAdvanced.setCourse(course);
        leoAdvanced.setName("Advanced Requirements Engineering");
        leoAdvanced.setDescription("Student can model and manage complex requirements.");
        leoAdvanced.setTopic("Requirements Engineering");
        leoAdvanced.setIsActive(true);
        leoAdvanced.setCreatedBy(teacher1);
        leoAdvanced = leoRepository.save(leoAdvanced);

        // =========================
        // 6. LEO DEPENDENCY
        // =========================

        LeoDependency dependency = new LeoDependency();
        dependency.setDependentLeo(leoAdvanced);
        dependency.setPrerequisiteLeo(leoBasics);
        dependency.setDependencyType(DependencyType.PREREQUISITE);
        leoDependencyRepository.save(dependency);

        // =========================
        // 7. ASSESSMENTS
        // =========================

        // student1: Basics REACHED
        Assessment assessment1 = new Assessment();
        assessment1.setStudent(student1);
        assessment1.setLeo(leoBasics);
        assessment1.setAssessedBy(teacher1);
        assessment1.setStatus(AssessmentStatus.REACHED.name());
        assessment1.setNotes("Student has successfully reached the basic learning outcome.");
        assessment1.setIsArchived(false);
        assessment1.setAssessedAt(LocalDateTime.now().minusDays(1));
        assessmentRepository.save(assessment1);


        Assessment assessment2 = new Assessment();
        assessment2.setStudent(student2);
        assessment2.setLeo(leoBasics);
        assessment2.setAssessedBy(teacher1);

        assessment2.setStatus(AssessmentStatus.REACHED.name());
        assessment2.setNotes("Student is working on the basics.");
        assessment2.setIsArchived(false);
        assessment2.setAssessedAt(LocalDateTime.now().minusHours(5));
        assessmentRepository.save(assessment2);

        // =========================
        // 8. SUGGESTIONS
        // =========================

        Suggestion suggestion1 = new Suggestion();
        suggestion1.setStudent(student1);
        suggestion1.setLeo(leoAdvanced);
        suggestion1.setSuggestedReason("ALL prerequisites for this LEO are reached. Student is ready for advanced content.");
        suggestion1.setPriority(1);
        suggestion1.setPrerequisitesMet(1);
        suggestion1.setTotalPrerequisites(1);
        suggestion1.setReadinessScore(BigDecimal.valueOf(95.00));
        suggestion1.setLastPrerequisiteReachedDaysAgo(0L);
        suggestion1.setIsDismissed(false);
        suggestionRepository.save(suggestion1);

        Suggestion suggestion2 = new Suggestion();
        suggestion2.setStudent(student2);
        suggestion2.setLeo(leoAdvanced);
        suggestion2.setSuggestedReason("Student has started the prerequisite but has not fully reached it yet.");
        suggestion2.setPriority(2);
        suggestion2.setPrerequisitesMet(0);
        suggestion2.setTotalPrerequisites(1);
        suggestion2.setReadinessScore(BigDecimal.valueOf(60.00));
        suggestion2.setLastPrerequisiteReachedDaysAgo(0L);
        suggestion2.setIsDismissed(false);
        suggestionRepository.save(suggestion2);
    }
}
