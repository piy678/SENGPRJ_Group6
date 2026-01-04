package com.sensprj.leo.repository;

import com.sensprj.leo.entity.AssessmentArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssessmentArchiveRepository extends JpaRepository<AssessmentArchive, Long> {
    List<AssessmentArchive> findByAssessmentId(Long assessmentId);

    @Query("SELECT a FROM AssessmentArchive a WHERE a.assessment.id = ?1 ORDER BY a.archivedAt DESC")
    List<AssessmentArchive> findHistoryByAssessmentId(Long assessmentId);

    long deleteByAssessmentId(Long assessmentId);

}