package com.shinhan.pda_midterm_project.domain.scrap_group.service;

import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.repository.MemberRepository;
import com.shinhan.pda_midterm_project.domain.scrap_group.model.ScrapGroup; // ✅ import
import com.shinhan.pda_midterm_project.domain.scrap_group.repository.ScrapGroupRepository; // ✅ import
import com.shinhan.pda_midterm_project.presentation.scrapGroup.dto.ScrapGroupResponseDto; // ✅ import
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; // ✅ import
import java.util.stream.Collectors; // ✅ import

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ScrapGroupServiceImpl implements ScrapGroupService { // ✅ 인터페이스 구현
    private final ScrapGroupRepository scrapGroupRepository; // ✅ ScrapGroupRepository 주입
    private final MemberRepository memberRepository;
    @Override
    public List<ScrapGroupResponseDto> getScrapGroup(Long memberId) {
        // 1. Repository를 통해 memberId에 해당하는 스크랩 그룹 목록을 조회
        List<ScrapGroup> scrapGroups = scrapGroupRepository.findAllByMemberIdOrderByCreatedAtAsc(memberId);

        // 2. 조회된 엔티티 목록을 DTO 목록으로 변환
        return scrapGroups.stream()
                .map(ScrapGroupResponseDto::new) // scrapGroup -> new ScrapGroupResponseDto(scrapGroup)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional // 데이터를 생성/수정/삭제하는 메서드에는 @Transactional을 붙여야 합니다.
    public ScrapGroupResponseDto createScrapGroup(Long memberId, String scrapGroupName){
        // 1. memberId로 Member 엔티티를 데이터베이스에서 찾습니다.
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다. id=" + memberId));

        // 2. 새로운 ScrapGroup 엔티티를 생성합니다.
        ScrapGroup newScrapGroup = ScrapGroup.create(member, scrapGroupName);

        // 3. Repository를 통해 데이터베이스에 저장합니다.
        ScrapGroup savedScrapGroup = scrapGroupRepository.save(newScrapGroup);

        // 4. 저장된 엔티티를 DTO로 변환하여 반환합니다.
        return new ScrapGroupResponseDto(savedScrapGroup);
    }
}