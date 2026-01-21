package com.sensprj.leo.service;


import com.sensprj.leo.entity.*;
import com.sensprj.leo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssessmentServiceTest {

    private AssessmentRepository assessmentRepository;
    private AssessmentArchiveRepository assessmentArchiveRepository;
    private CourseRepository courseRepository;
    private CourseEnrollmentRepository enrollmentRepository;
    private LeoRepository leoRepository;
    private LeoDependencyRepository dependencyRepository;

    private AssessmentService service;

    @BeforeEach
    void setUp() {
        assessmentRepository = mock(AssessmentRepository.class);
        assessmentArchiveRepository = mock(AssessmentArchiveRepository.class);
        courseRepository = mock(CourseRepository.class);
        enrollmentRepository = mock(CourseEnrollmentRepository.class);
        leoRepository = mock(LeoRepository.class);
        dependencyRepository = mock(LeoDependencyRepository.class);

        service = new AssessmentService(
                assessmentRepository,
                assessmentArchiveRepository,
                courseRepository,
                enrollmentRepository,
                leoRepository,
                dependencyRepository
        );
    }

    @Test
    void calculateGradeForStudent_missingAssessmentsDefaultToUnmarked_meansNotReached() {
        // Arrange
        long courseId = 1L;
        long studentId = 10L;

        Course c = new Course();
        c.setId(courseId);

        Leo leo1 = new Leo();
        leo1.setId(100L);
        leo1.setCourse(c);

        Leo leo2 = new Leo();
        leo2.setId(200L);
        leo2.setCourse(c);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(c));
        when(leoRepository.findByCourse(c)).thenReturn(List.of(leo1, leo2));

        // No prerequisites
        when(dependencyRepository.findByDependentLeoId(100L)).thenReturn(List.of());
        when(dependencyRepository.findByDependentLeoId(200L)).thenReturn(List.of());

        when(assessmentRepository.findByStudentIdAndLeoIdAndIsArchivedFalse(studentId, 100L))
                .thenReturn(Optional.empty());
        when(assessmentRepository.findByStudentIdAndLeoIdAndIsArchivedFalse(studentId, 200L))
                .thenReturn(Optional.empty());

        // Act
        AssessmentService.GradeDto dto = service.calculateGradeForStudent(courseId, studentId);

        // Assert
        assertEquals(0.0, dto.score(), 1e-9);
        assertEquals(5.0, dto.grade(), 1e-9);
        assertEquals(0L, dto.reached());
        assertEquals(2L, dto.totalUnlocked());
    }

    @Test
    void createDefaultNotReachedForLeo_createsAssessmentsWithStatusUnmarked() {
        // Arrange
        Course course = new Course();
        course.setId(1L);

        Leo leo = new Leo();
        leo.setId(100L);
        leo.setCourse(course);

        User teacher = new User();
        teacher.setId(99L);

        User s1 = new User(); s1.setId(1L);
        User s2 = new User(); s2.setId(2L);

        CourseEnrollment e1 = new CourseEnrollment();
        e1.setStudent(s1);
        e1.setCourse(course);

        CourseEnrollment e2 = new CourseEnrollment();
        e2.setStudent(s2);
        e2.setCourse(course);

        when(enrollmentRepository.findByCourse(course)).thenReturn(List.of(e1, e2));


        when(assessmentRepository.existsByStudentAndLeoAndIsArchivedFalse(s1, leo)).thenReturn(false);
        when(assessmentRepository.existsByStudentAndLeoAndIsArchivedFalse(s2, leo)).thenReturn(false);

        // Act
        int created = service.createDefaultNotReachedForLeo(leo, teacher);

        // Assert
        assertEquals(2, created);

        ArgumentCaptor<Assessment> captor = ArgumentCaptor.forClass(Assessment.class);
        verify(assessmentRepository, times(2)).save(captor.capture());

        List<Assessment> saved = captor.getAllValues();
        assertEquals("UNMARKED", saved.get(0).getStatus());
        assertEquals("UNMARKED", saved.get(1).getStatus());

        assertFalse(saved.get(0).getIsArchived());
        assertFalse(saved.get(1).getIsArchived());

        assertSame(leo, saved.get(0).getLeo());
        assertSame(leo, saved.get(1).getLeo());

        assertSame(teacher, saved.get(0).getAssessedBy());
        assertSame(teacher, saved.get(1).getAssessedBy());
    }
}