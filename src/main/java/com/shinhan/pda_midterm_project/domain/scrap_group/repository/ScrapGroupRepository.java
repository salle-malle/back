package com.shinhan.pda_midterm_project.domain.scrap_group.repository;

import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // ✅ List import

@Repository
public interface ScrapGroupRepository extends JpaRepository<ScrapGroup, Long> {

    /**
     * 특정 Member ID에 해당하는 모든 스크랩 그룹을 조회합니다.
     * 생성일(createdAt) 기준으로 오름차순 정렬합니다.
     * @param memberId 조회할 사용자의 ID
     * @return 스크랩 그룹 리스트
     */
    List<ScrapGroup> findAllByMemberIdOrderByCreatedAtAsc(Long memberId);
}