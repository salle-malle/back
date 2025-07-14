package com.shinhan.pda_midterm_project.domain.scrap_grouped.repository;

import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import com.shinhan.pda_midterm_project.domain.scrap_grouped.model.ScrapGrouped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
//
//@Repository
//public interface ScrapGroupedRepository extends JpaRepository<ScrapGrouped, Long> {
////    List<ScrapGroup> findAllByMemberIdScrapGroupIdOrderByCreatedAtAsc(Long memberId, Long scrapGroupId);
//    /**
//     * 특정 사용자의 특정 스크랩 그룹에 포함된 모든 스크랩 항목을 조회합니다.
//     * Fetch Join을 사용하여 N+1 문제를 해결합니다.
//     *
//     * @param memberId     사용자 ID
//     * @param scrapGroupId 스크랩 그룹 ID
//     * @return 스크랩된 항목(ScrapGrouped) 리스트
//     */
//    @Query("SELECT sgd FROM ScrapGrouped sgd " +
//            "JOIN FETCH sgd.memberStockSnapshot mss " +
//            "JOIN FETCH mss.investmentTypeNewsComment itnc " +
//            "JOIN FETCH itnc.summary s " +
//            "JOIN FETCH s.stock " +
//            "WHERE sgd.scrapGroup.id = :scrapGroupId AND sgd.scrapGroup.member.id = :memberId " +
//            "ORDER BY sgd.createdAt ASC")
//    List<ScrapGrouped> findScrapsInGroup(@Param("memberId") Long memberId, @Param("scrapGroupId") Long scrapGroupId);
//}

import com.shinhan.pda_midterm_project.domain.scrap_grouped.model.ScrapGrouped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapGroupedRepository extends JpaRepository<ScrapGrouped, Long> {

    /**
     * ✅ 요청하신 로직을 구현한 새로운 JPQL 쿼리 메서드입니다.
     * 특정 멤버의 스크랩된(user_scrap=true) 스냅샷 중, 특정 그룹에 속한 항목만 조회합니다.
     *
     * @param memberId     사용자 ID
     * @param scrapGroupId 스크랩 그룹 ID
     * @return 스크랩된 항목(ScrapGrouped) 리스트
     */
    @Query("SELECT sgd FROM ScrapGrouped sgd " +
            "JOIN FETCH sgd.memberStockSnapshot mss " +
            "JOIN FETCH sgd.scrapGroup sg " +
            "JOIN FETCH mss.investmentTypeNewsComment itnc " +
            "JOIN FETCH itnc.summary s " +
            "JOIN FETCH s.stock " +
            "WHERE sg.id = :scrapGroupId " +           // 2. 특정 스크랩 그룹 ID와 일치하고,
            "AND mss.member.id = :memberId " +       // 1. 스냅샷의 소유주가 특정 멤버이며,
            "AND mss.userScrap = true " +            // 1. 해당 스냅샷이 스크랩(true) 상태일 때
            "ORDER BY sgd.createdAt ASC")
    List<ScrapGrouped> findScrapsInGroup(@Param("memberId") Long memberId, @Param("scrapGroupId") Long scrapGroupId);
}