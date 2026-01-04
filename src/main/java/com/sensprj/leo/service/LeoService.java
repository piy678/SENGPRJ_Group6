package com.sensprj.leo.service;

import com.sensprj.leo.entity.Leo;
import com.sensprj.leo.entity.LeoDependency;
import com.sensprj.leo.repository.LeoDependencyRepository;
import com.sensprj.leo.repository.LeoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LeoService {

    private final LeoRepository leoRepository;
    private final LeoDependencyRepository dependencyRepository;
    private final AssessmentService assessmentService;

    @Transactional
    public void deleteLeo(Long leoId, boolean force) {
        Leo leo = leoRepository.findById(leoId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<LeoDependency> dependents = dependencyRepository.findByPrerequisiteLeoId(leoId);

        if (!force && !dependents.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "DEPENDENCIES_EXIST");
        }


        dependencyRepository.deleteByPrerequisiteLeoId(leoId);
        dependencyRepository.deleteByDependentLeoId(leoId);


        assessmentService.deleteAllForLeo(leoId);


        leoRepository.deleteById(leoId);
    }
}
