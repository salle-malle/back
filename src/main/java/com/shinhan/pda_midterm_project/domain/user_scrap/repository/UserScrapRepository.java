package com.shinhan.pda_midterm_project.domain.user_scrap.repository;

import com.shinhan.pda_midterm_project.domain.user_scrap.model.UserScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserScrapRepository extends JpaRepository<UserScrap, Long> {
}