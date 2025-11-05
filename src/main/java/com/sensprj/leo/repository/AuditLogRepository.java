package com.sensprj.leo.repository;

import com.sensprj.leo.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUserId(Long userId);
    List<AuditLog> findByAssessmentId(Long assessmentId);
    List<AuditLog> findByLeoId(Long leoId);
    List<AuditLog> findByAction(String action);
}