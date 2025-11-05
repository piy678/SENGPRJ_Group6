package com.sensprj.leo.repository;

import com.sensprj.leo.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    List<Suggestion> findByStudentId(Long studentId);
    List<Suggestion> findByLeoId(Long leoId);
    Optional<Suggestion> findByStudentIdAndLeoId(Long studentId, Long leoId);
    List<Suggestion> findByStudentIdAndIsDismissed(Long studentId, Boolean isDismissed);
}