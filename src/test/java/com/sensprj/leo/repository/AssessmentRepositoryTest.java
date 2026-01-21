package com.sensprj.leo.repository;

import com.sensprj.leo.entity.Assessment;
import com.sensprj.leo.entity.Course;
import com.sensprj.leo.entity.Leo;
import com.sensprj.leo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AssessmentRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AssessmentRepository assessmentRepository;

    // ---------- helpers ----------

    private LocalDateTime now() {
        return LocalDateTime.now();
    }

    private User mkUser(String username, String role) {
        User u = new User();
        u.setUsername(username);
        u.setEmail(username + "@test.com");
        u.setPasswordHash("x");
        u.setRole(role);
        u.setIsActive(true);

        // NOT NULL columns
        u.setCreatedAt(now());
        u.setUpdatedAt(now());

        em.persist(u);
        em.flush();
        return u;
    }

    private Course mkCourse(User teacher, String name) {
        Course c = new Course();
        c.setName(name);
        c.setDescription("Test course");
        c.setIsActive(true);
        c.setTeacher(teacher);

        c.setCreatedAt(now());
        c.setUpdatedAt(now());

        em.persist(c);
        em.flush();
        return c;
    }

    private Leo mkLeo(Course course, User teacher, String name) {
        Leo leo = new Leo();
        leo.setName(name);
        leo.setDescription("Test leo");
        leo.setTopic("Test topic");
        leo.setIsActive(true);
        leo.setCourse(course);
        leo.setCreatedBy(teacher);

        leo.setCreatedAt(now());
        leo.setUpdatedAt(now());

        em.persist(leo);
        em.flush();
        return leo;
    }

    private Assessment mkAssessment(User student, User teacher, Leo leo, String status, boolean archived) {
        Assessment a = new Assessment();
        a.setStudent(student);
        a.setAssessedBy(teacher);
        a.setLeo(leo);
        a.setStatus(status);
        a.setIsArchived(archived);

        a.setAssessedAt(now());
        a.setCreatedAt(now());
        a.setUpdatedAt(now());

        em.persist(a);
        em.flush();
        return a;
    }

    // ---------- tests ----------

    @Test
    void findByStudentIdAndLeoIdAndIsArchivedFalse_returnsOnlyActive() {
        User teacher = mkUser("teacher1", "TEACHER");
        User student = mkUser("student1", "STUDENT");

        Course course = mkCourse(teacher, "SE Project");
        Leo leo = mkLeo(course, teacher, "LEO1");

        Assessment active = mkAssessment(student, teacher, leo, "UNMARKED", false);
        mkAssessment(student, teacher, leo, "REACHED", true);

        var result = assessmentRepository.findByStudentIdAndLeoIdAndIsArchivedFalse(student.getId(), leo.getId());

        assertTrue(result.isPresent());
        assertEquals(active.getId(), result.get().getId());
        assertFalse(result.get().getIsArchived());
    }

    @Test
    void findActiveAssessmentsByStudentAndCourse_filtersCourseAndArchived() {
        User teacher = mkUser("teacher2", "TEACHER");
        User student = mkUser("student2", "STUDENT");

        Course courseA = mkCourse(teacher, "Course A");
        Course courseB = mkCourse(teacher, "Course B");

        Leo leoA = mkLeo(courseA, teacher, "LEO-A");
        Leo leoB = mkLeo(courseB, teacher, "LEO-B");

        Assessment a1 = mkAssessment(student, teacher, leoA, "REACHED", false);
        mkAssessment(student, teacher, leoA, "REACHED", true);
        mkAssessment(student, teacher, leoB, "REACHED", false);

        List<Assessment> res = assessmentRepository.findActiveAssessmentsByStudentAndCourse(
                student.getId(),
                courseA.getId()
        );

        assertEquals(1, res.size());
        assertEquals(a1.getId(), res.get(0).getId());
        assertFalse(res.get(0).getIsArchived());
        assertEquals(courseA.getId(), res.get(0).getLeo().getCourse().getId());
    }

    @Test
    void countReachedLeosByStudent_countsOnlyReachedAndActive() {
        User teacher = mkUser("teacher3", "TEACHER");
        User student = mkUser("student3", "STUDENT");
        Course course = mkCourse(teacher, "Course C");

        Leo leo1 = mkLeo(course, teacher, "LEO-1");
        Leo leo2 = mkLeo(course, teacher, "LEO-2");
        Leo leo3 = mkLeo(course, teacher, "LEO-3");

        mkAssessment(student, teacher, leo1, "REACHED", false);
        mkAssessment(student, teacher, leo2, "REACHED", true);
        mkAssessment(student, teacher, leo3, "UNMARKED", false);

        Integer count = assessmentRepository.countReachedLeosByStudent(student.getId());

        assertEquals(1, count);
    }
}
