package com.sensprj.leo.repository;

import com.sensprj.leo.entity.Leo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeoRepository extends JpaRepository<Leo, Long> {
    List<Leo> findByCourseId(Long courseId);
    List<Leo> findByIsActive(Boolean isActive);
    List<Leo> findByCourseIdAndIsActive(Long courseId, Boolean isActive);
}