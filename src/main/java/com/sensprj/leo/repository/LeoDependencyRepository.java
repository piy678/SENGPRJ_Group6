package com.sensprj.leo.repository;

import com.sensprj.leo.entity.Leo;
import com.sensprj.leo.entity.LeoDependency;
import com.sensprj.leo.entity.enums.DependencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeoDependencyRepository extends JpaRepository<LeoDependency, Long> {

    List<LeoDependency> findByDependentLeo(Leo dependentLeo);

    List<LeoDependency> findByDependentLeoId(Long dependentLeoId);

    List<LeoDependency> findByPrerequisiteLeo(Leo prerequisiteLeo);

    List<LeoDependency> findByPrerequisiteLeoId(Long prerequisiteLeoId);

    boolean existsByDependentLeoAndPrerequisiteLeo(Leo dependentLeo, Leo prerequisiteLeo);

    boolean existsByDependentLeoIdAndPrerequisiteLeoId(Long dependentLeoId, Long prerequisiteLeoId);

    Optional<LeoDependency> findByDependentLeoIdAndPrerequisiteLeoId(
            Long dependentLeoId,
            Long prerequisiteLeoId
    );

    long deleteByDependentLeoId(Long dependentLeoId);

    long deleteByPrerequisiteLeoId(Long prerequisiteLeoId);

    List<LeoDependency> findByDependentLeoIdAndDependencyType(
            Long dependentLeoId,
            DependencyType dependencyType
    );

    List<LeoDependency> findByPrerequisiteLeoIdAndDependencyType(
            Long prerequisiteLeoId,
            DependencyType dependencyType
    );
}
