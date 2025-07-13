package com.shinhan.pda_midterm_project.domain.scrap_group.repository;

import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapGroupRepository extends JpaRepository<ScrapGroup, Long> {
}