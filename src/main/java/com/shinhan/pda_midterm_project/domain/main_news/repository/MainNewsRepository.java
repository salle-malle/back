package com.shinhan.pda_midterm_project.domain.main_news.repository;

import com.shinhan.pda_midterm_project.domain.main_news.model.MainNews;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainNewsRepository extends JpaRepository<MainNews, Long> {
    List<MainNews> findAllByOrderByIdDesc(Pageable pageable);
}
