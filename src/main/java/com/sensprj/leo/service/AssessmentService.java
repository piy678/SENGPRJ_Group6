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
                        .orElse("NOT_REACHED");

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
                    .orElse("NOT_REACHED");

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
                        a.setStatus("NOT_REACHED");
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


        Assessment existing =
                assessmentRepository
                        .findFirstByStudentIdAndLeoId(student.getId(), leo.getId())
                        .orElse(null);

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
            existing.setAssessedAt(LocalDateTime.now());
            existing.setIsArchived(false);
            assessmentRepository.save(existing);

        } else {
            Assessment a = new Assessment();
            a.setStudent(student);
            a.setLeo(leo);
            a.setAssessedBy(teacher);
            a.setStatus(status.name());
            a.setAssessedAt(assessedAt != null ? assessedAt : LocalDateTime.now());
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

