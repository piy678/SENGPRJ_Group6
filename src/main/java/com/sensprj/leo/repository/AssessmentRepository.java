package com.sensprj.leo.repository;

import com.sensprj.leo.entity.Assessment;
import com.sensprj.leo.entity.Course;
import com.sensprj.leo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByStudentIdAndLeoId(Long studentId, Long leoId);
    List<Assessment> findByStudentId(Long studentId);
    List<Assessment> findByLeoId(Long leoId);
    List<Assessment> findByIsArchived(Boolean isArchived);
    List<Assessment> findByLeoCourse(Course course);
    List<Assessment> findByStudent(User student);


    @Query("SELECT a FROM Assessment a WHERE a.student.id = ?1 AND a.leo.course.id = ?2 AND a.isArchived = false")
    List<Assessment> findActiveAssessmentsByStudentAndCourse(Long studentId, Long courseId);

    @Query("SELECT COUNT(a) FROM Assessment a WHERE a.student.id = ?1 AND a.status = 'REACHED' AND a.isArchived = false")
    Integer countReachedLeosByStudent(Long studentId);
}
