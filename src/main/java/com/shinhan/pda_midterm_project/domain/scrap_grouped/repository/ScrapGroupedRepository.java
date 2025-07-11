package com.shinhan.pda_midterm_project.domain.scrap_grouped.repository;

import com.shinhan.pda_midterm_project.domain.scrap_grouped.model.ScrapGrouped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapGroupedRepository extends JpaRepository<ScrapGrouped, Long> {
}