package com.sensprj.leo.service;

import com.sensprj.leo.entity.*;
import com.sensprj.leo.entity.enums.AssessmentStatus;
import com.sensprj.leo.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import com.sensprj.leo.repository.LeoDependencyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentArchiveRepository assessmentArchiveRepository;
    private final CourseRepository courseRepository;
    private final CourseEnrollmentRepository enrollmentRepository;
    private final LeoRepository leoRepository;
    private final LeoDependencyRepository dependencyRepository;

    public record GradeDto(double score, double grade, long reached, long totalUnlocked) {}

    public GradeDto calculateGradeForStudent(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        List<Leo> leos = leoRepository.findByCourse(course);

        long totalUnlocked = 0;
        long reached = 0;

        for (Leo leo : leos) {


            List<LeoDependency> prereqs = dependencyRepository.findByDependentLeoId(leo.getId());
            boolean ready = true;

            for (LeoDependency dep : prereqs) {
                Long prereqLeoId = dep.getPrerequisiteLeo().getId();
                String prereqStatus = assessmentRepository
                        .findByStudentIdAndLeoIdAndIsArchivedFalse(studentId, prereqLeoId)
                        .map(Assessment::getStatus)
                        .orElse("UNMARKED");


                if (!"REACHED".equalsIgnoreCase(prereqStatus)) {
                    ready = false;
                    break;
                }
            }

            if (!ready) continue;

            totalUnlocked++;

            String status = assessmentRepository
                    .findByStudentIdAndLeoIdAndIsArchivedFalse(studentId, leo.getId())
                    .map(Assessment::getStatus)
                    .orElse("UNMARKED");

            if ("REACHED".equalsIgnoreCase(status)) reached++;
        }

        double score = totalUnlocked == 0 ? 0.0 : (double) reached / (double) totalUnlocked;
        double grade = scoreToGermanGrade(score);

        return new GradeDto(score, grade, reached, totalUnlocked);
    }


    private double scoreToGermanGrade(double score) {
        if (score >= 0.90) return 1.0;
        if (score >= 0.80) return 2.0;
        if (score >= 0.65) return 3.0;
        if (score >= 0.50) return 4.0;
        return 5.0;
    }


    @Transactional
    public int createDefaultNotReachedForLeo(Leo leo, User teacher) {
        List<CourseEnrollment> enrollments = enrollmentRepository.findByCourse(leo.getCourse());

        int created = 0;
        for (CourseEnrollment e : enrollments) {
            User student = e.getStudent();

            boolean exists = assessmentRepository.existsByStudentAndLeoAndIsArchivedFalse(student, leo);
            if (!exists) {
                Assessment a = new Assessment();
                a.setStudent(student);
                a.setLeo(leo);
                a.setAssessedBy(teacher);
                a.setStatus("UNMARKED");
                a.setIsArchived(false);

                assessmentRepository.save(a);
                created++;
            }
        }
        return created;
    }


    @Transactional
    public int backfillNotReachedForAll(User teacher) {
        int created = 0;

        for (Course course : courseRepository.findAll()) {

            var enrollments = enrollmentRepository.findByCourse(course);
            var leos = leoRepository.findByCourse(course);

            for (var enr : enrollments) {
                var student = enr.getStudent();

                for (var leo : leos) {

                    boolean exists = assessmentRepository
                            .existsByStudentAndLeoAndIsArchivedFalse(student, leo);

                    if (!exists) {
                        Assessment a = new Assessment();
                        a.setStudent(student);
                        a.setLeo(leo);
                        a.setAssessedBy(teacher);
                        a.setStatus("UNMARKED");
                        a.setIsArchived(false);

                        assessmentRepository.save(a);
                        created++;
                    }
                }
            }
        }
        return created;
    }



    public void upsertAssessment(
            User student,
            Leo leo,
            User teacher,
            AssessmentStatus status,
            LocalDateTime assessedAt
    ) {
        // 1) Status vorher merken (wichtig für Downgrade-Erkennung)
        Assessment existing =
                assessmentRepository
                        .findFirstByStudentIdAndLeoId(student.getId(), leo.getId())
                        .orElse(null);

        AssessmentStatus oldStatus = (existing != null)
                ? AssessmentStatus.valueOf(existing.getStatus())
                : AssessmentStatus.UNMARKED;

        // 2) Prerequisite-Check: nur bewertbar, wenn alle prereqs REACHED sind
        List<LeoDependency> prereqs = dependencyRepository.findByDependentLeoId(leo.getId());

        for (LeoDependency dep : prereqs) {
            Long prereqLeoId = dep.getPrerequisiteLeo().getId();

            String prereqStatus = assessmentRepository
                    .findByStudentIdAndLeoIdAndIsArchivedFalse(student.getId(), prereqLeoId)
                    .map(Assessment::getStatus)
                    .orElse("UNMARKED");

            if (!"REACHED".equalsIgnoreCase(prereqStatus)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "LEO is locked. Prerequisite not reached: " + prereqLeoId
                );
            }
        }

        // 3) Upsert + Archive
        if (existing != null) {
            AssessmentArchive archive = new AssessmentArchive();
            archive.setAssessment(existing);
            archive.setOldStatus(existing.getStatus());
            archive.setArchivedAt(LocalDateTime.now());
            archive.setArchivedBy(teacher);
            archive.setReason("Updated by teacher");
            assessmentArchiveRepository.save(archive);

            existing.setStatus(status.name());
            existing.setAssessedBy(teacher);
            existing.setAssessedAt(assessedAt != null ? assessedAt : LocalDateTime.now());
            existing.setIsArchived(false);
            assessmentRepository.save(existing);
            assessmentRepository.flush();

        } else {
            Assessment a = new Assessment();
            a.setStudent(student);
            a.setLeo(leo);
            a.setAssessedBy(teacher);
            a.setStatus(status.name());
            a.setAssessedAt(assessedAt != null ? assessedAt : LocalDateTime.now());
            a.setIsArchived(false);
            assessmentRepository.save(a);
            assessmentRepository.flush();

        }

        // 4) Cascade Reset: Wenn dieses LEO von REACHED weg geändert wurde,
        // dann alle Dependents zurücksetzen (wenn sie dadurch gelockt werden)
        boolean reachedDowngraded = (oldStatus == AssessmentStatus.REACHED && status != AssessmentStatus.REACHED);
        if (status != AssessmentStatus.REACHED) {
            cascadeResetDependents(student, leo, teacher);
        }

    }

    /**
     * Setzt alle (direkten & indirekten) Dependents auf NOT_REACHED (oder UNMARKED),
     * wenn sie wegen fehlender prerequisites nicht mehr "unlocked" sind.
     */
    private void cascadeResetDependents(User student, Leo changedLeo, User teacher) {
        java.util.ArrayDeque<Long> queue = new java.util.ArrayDeque<>();
        java.util.HashSet<Long> visited = new java.util.HashSet<>();

        // starte bei "changedLeo"
        queue.add(changedLeo.getId());

        while (!queue.isEmpty()) {
            Long prereqLeoId = queue.poll();
            if (!visited.add(prereqLeoId)) continue;

            // alle LEOs, die prereqLeoId als Voraussetzung haben
            List<LeoDependency> dependents = dependencyRepository.findByPrerequisiteLeoId(prereqLeoId);

            for (LeoDependency dep : dependents) {
                Leo dependentLeo = dep.getDependentLeo();

                // wenn dependent nicht mehr unlocked ist => reset
                boolean unlocked = isUnlocked(student.getId(), dependentLeo.getId());
                if (!unlocked) {
                    forceSetStatus(student, dependentLeo, teacher, AssessmentStatus.UNMARKED,
                            "Auto-reset: prerequisite not reached anymore");

                }

                // weiter kaskadieren (auch wenn schon reset/unlocked)
                queue.add(dependentLeo.getId());
            }
        }
    }

    /**
     * true, wenn ALLE prerequisites des LEOs REACHED sind.
     */
    private boolean isUnlocked(Long studentId, Long leoId) {
        List<LeoDependency> prereqs = dependencyRepository.findByDependentLeoId(leoId);

        for (LeoDependency dep : prereqs) {
            Long prereqId = dep.getPrerequisiteLeo().getId();

            String prereqStatus = assessmentRepository
                    .findByStudentIdAndLeoIdAndIsArchivedFalse(studentId, prereqId)
                    .map(Assessment::getStatus)
                    .orElse("UNMARKED");

            if (!"REACHED".equalsIgnoreCase(prereqStatus)) return false;
        }
        return true;
    }

    public boolean isLeoUnlockedForStudent(User student, Leo leo) {
        List<LeoDependency> prereqs = dependencyRepository.findByDependentLeoId(leo.getId());

        for (LeoDependency dep : prereqs) {
            Long prereqLeoId = dep.getPrerequisiteLeo().getId();

            String prereqStatus = assessmentRepository
                    .findByStudentIdAndLeoIdAndIsArchivedFalse(student.getId(), prereqLeoId)
                    .map(Assessment::getStatus)
                    .orElse("UNMARKED");

            if (!"REACHED".equalsIgnoreCase(prereqStatus)) return false;
        }
        return true;
    }


    private void forceSetStatus(
            User student,
            Leo leo,
            User teacher,
            AssessmentStatus newStatus,
            String reason
    ) {
        Assessment existing = assessmentRepository
                .findFirstByStudentIdAndLeoId(student.getId(), leo.getId())
                .orElse(null);

        if (existing != null) {
            // wenn schon so gesetzt: nichts tun
            AssessmentStatus current = AssessmentStatus.valueOf(existing.getStatus());
            if (current == newStatus) return;

            AssessmentArchive archive = new AssessmentArchive();
            archive.setAssessment(existing);
            archive.setOldStatus(existing.getStatus());
            archive.setArchivedAt(LocalDateTime.now());
            archive.setArchivedBy(teacher);
            archive.setReason(reason);
            assessmentArchiveRepository.save(archive);

            existing.setStatus(newStatus.name());
            existing.setAssessedBy(teacher);
            existing.setAssessedAt(LocalDateTime.now());
            existing.setIsArchived(false);
            assessmentRepository.save(existing);

        } else {
            
            Assessment a = new Assessment();
            a.setStudent(student);
            a.setLeo(leo);
            a.setAssessedBy(teacher);
            a.setStatus(newStatus.name());
            a.setAssessedAt(LocalDateTime.now());
            a.setIsArchived(false);
            assessmentRepository.save(a);
        }
    }




    @Transactional
    public void deleteAllForLeo(Long leoId) {
        List<Assessment> assessments = assessmentRepository.findByLeoId(leoId);

        for (Assessment a : assessments) {
            a.setPreviousAssessment(null);
        }
        assessmentRepository.saveAll(assessments);

        for (Assessment a : assessments) {
            assessmentArchiveRepository.deleteByAssessmentId(a.getId());
        }

        assessmentRepository.deleteAll(assessments);
    }


}

