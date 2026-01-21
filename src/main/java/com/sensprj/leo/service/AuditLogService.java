package com.sensprj.leo.service;

import com.sensprj.leo.entity.AuditLog;
import com.sensprj.leo.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepository repo;

    public AuditLogService(AuditLogRepository repo) {
        this.repo = repo;
    }

    public void logPermissionRejected(String username, String action, String status) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setIsSuccessful(false);
        log.setReason(status);
        // falls du userId brauchst: hier per UserRepository username->id holen
        repo.save(log);
    }
}
