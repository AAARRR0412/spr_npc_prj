package com.spr.socialtv.repository;

import com.spr.socialtv.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    Optional<Program> findBySubjectAndReleaseDate(String subject, String releaseDate);
}
