package com.sensprj.leo.service;

import com.sensprj.leo.entity.Assessment;
import com.sensprj.leo.entity.AssessmentArchive;
import com.sensprj.leo.entity.Leo;
import com.sensprj.leo.entity.User;
import com.sensprj.leo.entity.enums.AssessmentStatus;
import com.sensprj.leo.repository.AssessmentArchiveRepository;
import com.sensprj.leo.repository.AssessmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentArchiveRepository assessmentArchiveRepository;

    public void upsertAssessment(
            User student,
            Leo leo,
            User teacher,
            AssessmentStatus status,
            LocalDateTime assessedAt) {

        Assessment existing =
                assessmentRepository
                        .findFirstByStudentIdAndLeoId(student.getId(), leo.getId())
                        .orElse(null);

        if (existing != null) {
            // History
            AssessmentArchive archive = new AssessmentArchive();
            archive.setAssessment(existing);
            archive.setOldStatus(existing.getStatus());
            archive.setArchivedAt(LocalDateTime.now());
            archive.setArchivedBy(teacher);
            archive.setReason("Updated by teacher");
            assessmentArchiveRepository.save(archive);

            // Update current
            existing.setStatus(status.name());
            existing.setAssessedBy(teacher);
            existing.setAssessedAt(LocalDateTime.now());
            existing.setIsArchived(false);
            assessmentRepository.save(existing);

        } else {
            Assessment a = new Assessment();
            a.setStudent(student);
            a.setLeo(leo);
            a.setAssessedBy(teacher);
            a.setStatus(status.name());
            a.setAssessedAt(assessedAt);
            a.setAssessedAt(LocalDateTime.now());
            a.setIsArchived(false);
            assessmentRepository.save(a);
        }
    }
}

