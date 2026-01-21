package com.sensprj.leo.controller;

import com.sensprj.leo.entity.Assessment;
import com.sensprj.leo.entity.Course;
import com.sensprj.leo.entity.CourseEnrollment;
import com.sensprj.leo.entity.User;
import com.sensprj.leo.repository.AssessmentRepository;
import com.sensprj.leo.repository.CourseEnrollmentRepository;
import com.sensprj.leo.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.LENIENT)
class ProgressControllerTest {

    @Mock CourseRepository courseRepository;
    @Mock CourseEnrollmentRepository enrollmentRepository;
    @Mock AssessmentRepository assessmentRepository;

    ProgressController controller;

    @BeforeEach
    void setUp() {
        controller = new ProgressController(courseRepository, enrollmentRepository, assessmentRepository);
    }

    @Test
    void calculatesCountsAndProgress_mixedStatuses() {
        Long courseId = 1L;

        Course course = mock(Course.class);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Student A
        User s1 = mock(User.class);
        when(s1.getId()).thenReturn(10L);
        when(s1.getFirstName()).thenReturn("Max");
        when(s1.getLastName()).thenReturn("Mustermann");

        CourseEnrollment e1 = mock(CourseEnrollment.class);
        when(e1.getStudent()).thenReturn(s1);

        // Student B
        User s2 = mock(User.class);
        when(s2.getId()).thenReturn(20L);
        when(s2.getFirstName()).thenReturn("Erika");
        when(s2.getLastName()).thenReturn("Musterfrau");

        CourseEnrollment e2 = mock(CourseEnrollment.class);
        when(e2.getStudent()).thenReturn(s2);

        when(enrollmentRepository.findByCourse(course)).thenReturn(List.of(e1, e2));

        // Assessments (nur für s1)
        Assessment a1 = assessmentForStudentWithStatus(s1, "REACHED");
        Assessment a2 = assessmentForStudentWithStatus(s1, "partially_reached"); // case-insensitive
        Assessment a3 = assessmentForStudentWithStatus(s1, "NOT_REACHED");

        when(assessmentRepository.findByLeoCourse(course)).thenReturn(List.of(a1, a2, a3));

        var result = controller.getProgressForCourse(courseId);

        assertEquals(2, result.size());

        var dtoS1 = result.stream().filter(d -> d.getStudentId().equals(10L)).findFirst().orElseThrow();
        assertEquals("Max Mustermann", dtoS1.getName());
        assertEquals(1, dtoS1.getAchieved());
        assertEquals(1, dtoS1.getPartially());
        assertEquals(1, dtoS1.getNotAchieved());
        assertEquals(0, dtoS1.getUnmarked());
        assertEquals(3, dtoS1.getTotal());
        // progress = (achieved + partially) * 100 / total = (2*100)/3 = 66 (int division)
        assertEquals(66, dtoS1.getProgress());

        var dtoS2 = result.stream().filter(d -> d.getStudentId().equals(20L)).findFirst().orElseThrow();
        assertEquals(0, dtoS2.getTotal());
        assertEquals(0, dtoS2.getProgress());
    }

    @Test
    void treatsNullBlankAndInvalidStatusAsUnmarked() {
        Long courseId = 2L;

        Course course = mock(Course.class);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        User s1 = mock(User.class);
        when(s1.getId()).thenReturn(1L);
        when(s1.getFirstName()).thenReturn("A");
        when(s1.getLastName()).thenReturn("B");

        CourseEnrollment e1 = mock(CourseEnrollment.class);
        when(e1.getStudent()).thenReturn(s1);

        when(enrollmentRepository.findByCourse(course)).thenReturn(List.of(e1));

        Assessment a1 = assessmentForStudentWithStatus(s1, null);
        Assessment a2 = assessmentForStudentWithStatus(s1, "   ");
        Assessment a3 = assessmentForStudentWithStatus(s1, "something_weird");

        when(assessmentRepository.findByLeoCourse(course)).thenReturn(List.of(a1, a2, a3));

        var result = controller.getProgressForCourse(courseId);

        var dto = result.get(0);
        assertEquals(3, dto.getTotal());
        assertEquals(3, dto.getUnmarked());
        assertEquals(0, dto.getAchieved());
        assertEquals(0, dto.getPartially());
        assertEquals(0, dto.getNotAchieved());
        assertEquals(0, dto.getProgress()); // (0+0)*100/3 = 0
    }

    @Test
    void ignoresAssessmentsForStudentsNotEnrolled() {
        Long courseId = 3L;

        Course course = mock(Course.class);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        User enrolled = mock(User.class);
        when(enrolled.getId()).thenReturn(100L);
        when(enrolled.getFirstName()).thenReturn("En");
        when(enrolled.getLastName()).thenReturn("Rolled");

        CourseEnrollment e = mock(CourseEnrollment.class);
        when(e.getStudent()).thenReturn(enrolled);
        when(enrollmentRepository.findByCourse(course)).thenReturn(List.of(e));

        User notEnrolled = mock(User.class);
        when(notEnrolled.getId()).thenReturn(999L);

        Assessment foreign = assessmentForStudentWithStatus(notEnrolled, "REACHED");

        when(assessmentRepository.findByLeoCourse(course)).thenReturn(List.of(foreign));

        var result = controller.getProgressForCourse(courseId);

        var dto = result.get(0);
        assertEquals(0, dto.getTotal());
        assertEquals(0, dto.getAchieved());
        assertEquals(0, dto.getProgress());
    }

    @Test
    void throwsWhenCourseNotFound() {
        when(courseRepository.findById(404L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> controller.getProgressForCourse(404L));
    }

    private Assessment assessmentForStudentWithStatus(User student, String status) {
        Assessment a = mock(Assessment.class);
        when(a.getStudent()).thenReturn(student);
        lenient().when(a.getStatus()).thenReturn(status);
        return a;
    }

}
