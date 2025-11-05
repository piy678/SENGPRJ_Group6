package com.sensprj.leo.service;

import com.sensprj.leo.entity.*;
import com.sensprj.leo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
class CascadeService {

    private final LeoRepository leoRepository;
    private final AssessmentRepository assessmentRepository;

    /**
     * Trigger cascade when student reaches a LEO
     */
    public void triggerCascadeForStudentAndLeo(Long studentId, Long leoId) {
        log.info("Cascade triggered - Student: {}, LEO: {}", studentId, leoId);
        // TODO: Implement cascade logic later
    }

    /**
     * Check for circular dependencies
     */
    public boolean hasCircularDependency(Leo source, Leo target) {
        return detectCycle(source, target, new HashSet<>());
    }

    private boolean detectCycle(Leo current, Leo target, Set<Long> visited) {
        if (current.getId().equals(target.getId())) {
            return true;
        }
        if (visited.contains(current.getId())) {
            return false;
        }
        visited.add(current.getId());

        for (Leo prerequisite : current.getPrerequisites()) {
            if (detectCycle(prerequisite, target, visited)) {
                return true;
            }
        }
        return false;
    }
}
