package com.shinhan.pda_midterm_project.domain.news.repository;

import com.shinhan.pda_midterm_project.domain.news.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
}