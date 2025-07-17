package com.shinhan.pda_midterm_project.domain.total_summary.repository;

import com.shinhan.pda_midterm_project.domain.total_summary.model.TotalSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalSummaryRepository extends JpaRepository<TotalSummary, Long> {
}
