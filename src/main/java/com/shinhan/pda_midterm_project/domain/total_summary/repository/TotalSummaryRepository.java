package com.shinhan.pda_midterm_project.domain.total_summary.repository;

import com.shinhan.pda_midterm_project.domain.total_summary.model.TotalSummary;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalSummaryRepository extends JpaRepository<TotalSummary, Long> {

    @Query("SELECT y.totalContent FROM TotalSummary y WHERE y.member.id = :memberId AND DATE(y.createdAt) = DATE(:localDateTime)")
    String getTodayTotalSummary(@Param("localDateTime") LocalDateTime localDateTime,
                                @Param("memberId") Long memberId);

}
