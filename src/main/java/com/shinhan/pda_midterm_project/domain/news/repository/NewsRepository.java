package com.shinhan.pda_midterm_project.domain.news.repository;

import com.shinhan.pda_midterm_project.domain.news.model.News;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findAllByCreatedAtBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    // 종목코드로 뉴스 조회 (최신순으로 정렬)
    List<News> findByStock_StockIdOrderByCreatedAtDesc(String stockId);

    List<News> findByStock_StockIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            String stockId,
            LocalDateTime start,
            LocalDateTime end
    );

}