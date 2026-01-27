package com.sensprj.leo.repository;

import com.sensprj.leo.entity.Assessment;
import com.sensprj.leo.entity.Course;
import com.sensprj.leo.entity.Leo;
import com.sensprj.leo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByLeoId(Long leoId);
    List<Assessment> findByLeoCourse(Course course);
    List<Assessment> findByStudent(User student);

    boolean existsByStudentAndLeoAndIsArchivedFalse(User student, Leo leo);

    @Query("SELECT a FROM Assessment a WHERE a.student.id = ?1 AND a.leo.course.id = ?2 AND a.isArchived = false")
    List<Assessment> findActiveAssessmentsByStudentAndCourse(Long studentId, Long courseId);

    @Query("SELECT COUNT(a) FROM Assessment a WHERE a.student.id = ?1 AND a.status = 'REACHED' AND a.isArchived = false")
    Integer countReachedLeosByStudent(Long studentId);
    Optional<Assessment> findByStudentIdAndLeoIdAndIsArchivedFalse(
            Long studentId,
            Long leoId
    );

    Optional<Assessment> findFirstByStudentIdAndLeoId(Long studentId, Long leoId);
    @Query("""
  select a.leo.id as leoId, count(a) as reachedCount
  from Assessment a
  where a.leo.course.id = :courseId
    and a.status = 'REACHED'
    and a.isArchived = false
  group by a.leo.id
""")
    List<Object[]> countReachedByLeoInCourse(@Param("courseId") Long courseId);


}
