package com.shinhan.pda_midterm_project.domain.scrap.repository;

import com.shinhan.pda_midterm_project.domain.scrap.model.Scrap;
import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // ✅ List import

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {

}