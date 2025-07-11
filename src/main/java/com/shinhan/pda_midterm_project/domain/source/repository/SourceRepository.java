package com.shinhan.pda_midterm_project.domain.source.repository;

import com.shinhan.pda_midterm_project.domain.source.model.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepository extends JpaRepository<Source, Long> {
}