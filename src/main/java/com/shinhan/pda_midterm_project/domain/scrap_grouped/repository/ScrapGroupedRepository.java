package com.shinhan.pda_midterm_project.domain.scrap_grouped.repository;

import com.shinhan.pda_midterm_project.domain.scrap.model.Scrap;
import com.shinhan.pda_midterm_project.domain.scrap_grouped.model.ScrapGrouped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ScrapGroupedRepository extends JpaRepository<ScrapGrouped, Long> {

    @Query("SELECT sgd FROM ScrapGrouped sgd " +
            "JOIN FETCH sgd.scrap s " + // mss -> s (Scrap)
            "JOIN FETCH s.memberStockSnapshot mss " + // Scrap을 통해 mss에 접근
            "JOIN FETCH sgd.scrapGroup sg " +
            "JOIN FETCH mss.investmentTypeNewsComment itnc " +
            "JOIN FETCH itnc.summary sum " +
            "JOIN FETCH sum.stock " +
            "WHERE sg.id = :scrapGroupId " +
            "AND sg.member.id = :memberId " + // 그룹의 소유주로 확인
            "ORDER BY sgd.createdAt ASC")
    List<ScrapGrouped> findScrapsInGroup(@Param("memberId") Long memberId, @Param("scrapGroupId") Long scrapGroupId);

    boolean existsByScrapGroupIdAndScrapId(Long scrapGroupId, Long scrapId);

    void deleteAllByScrap(Scrap scrap);

    void deleteAllByScrapGroupId(Long scrapGroupId);

    @Modifying
    @Query("DELETE FROM ScrapGrouped sgd WHERE sgd.scrapGroup.id = :scrapGroupId")
    void bulkDeleteByScrapGroupId(@Param("scrapGroupId") Long scrapGroupId);

    @Query("SELECT sgd.scrapGroup.id FROM ScrapGrouped sgd WHERE sgd.scrap.id = :scrapId")
    Set<Long> findGroupIdsByScrapId(@Param("scrapId") Long scrapId);
    @Query("SELECT sg FROM ScrapGrouped sg " +
            "JOIN FETCH sg.scrap s " +
            "JOIN FETCH sg.scrapGroup " +
            "JOIN FETCH s.memberStockSnapshot mss " +
            "JOIN FETCH mss.investmentTypeNewsComment itnc " +
            "JOIN FETCH itnc.summary sum " +
            "JOIN FETCH sum.stock st " +
            "WHERE sg.scrapGroup.id = :scrapGroupId AND sg.scrapGroup.member.id = :memberId " +
            "ORDER BY sg.createdAt DESC")
    List<ScrapGrouped> findScrapsInGroupWithDetails(@Param("memberId") Long memberId, @Param("scrapGroupId") Long scrapGroupId);

}
