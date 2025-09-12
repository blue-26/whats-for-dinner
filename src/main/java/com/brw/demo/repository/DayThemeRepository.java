package com.brw.demo.repository;

import com.brw.demo.model.DayTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DayThemeRepository extends JpaRepository<DayTheme, Long> {
    Optional<DayTheme> findByName(String name);
}
