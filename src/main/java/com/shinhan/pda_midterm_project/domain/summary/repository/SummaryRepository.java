package com.shinhan.pda_midterm_project.domain.summary.repository;

import com.shinhan.pda_midterm_project.domain.summary.model.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummaryRepository extends JpaRepository<Summary, Long> {
}