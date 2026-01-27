package com.sensprj.leo.repository;

import com.sensprj.leo.entity.Course;
import com.sensprj.leo.entity.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {
    List<CourseEnrollment> findByStudentId(Long studentId);
    Optional<CourseEnrollment> findByCourseIdAndStudentId(Long courseId, Long studentId);
    List<CourseEnrollment> findByCourse(Course course);
    void deleteByCourseId(Long courseId);

}