package com.sensprj.leo.repository;

import com.sensprj.leo.entity.Suggestion;
import com.sensprj.leo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
}