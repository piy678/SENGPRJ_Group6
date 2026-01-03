package com.sensprj.leo.repository;

import com.sensprj.leo.entity.Leo;
import com.sensprj.leo.entity.LeoDependency;
import com.sensprj.leo.entity.enums.DependencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeoDependencyRepository extends JpaRepository<LeoDependency, Long> {

    List<LeoDependency> findByDependentLeo(Leo dependentLeo);

    List<LeoDependency> findByDependentLeoId(Long dependentLeoId);

    List<LeoDependency> findByPrerequisiteLeo(Leo prerequisiteLeo);


    boolean existsByDependentLeoAndPrerequisiteLeo(Leo dependentLeo, Leo prerequisiteLeo);
    long countByDependentLeoId(Long leoId);

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

    @Query("""
  select d.prerequisiteLeo.id, d.dependentLeo.id
  from LeoDependency d
  where d.dependentLeo.course.id = :courseId
    and d.dependencyType = com.sensprj.leo.entity.enums.DependencyType.PREREQUISITE
""")
    List<Object[]> findEdgesByCourseId(@Param("courseId") Long courseId);
    @Query("""
    select d.prerequisiteLeo.id, d.dependentLeo.id
    from LeoDependency d
    where d.dependentLeo.course.id = :courseId
  """)
    List<Object[]> findEdgesByCourse(@Param("courseId") Long courseId);
    List<LeoDependency> findByPrerequisiteLeoId(Long prerequisiteLeoId);
}
