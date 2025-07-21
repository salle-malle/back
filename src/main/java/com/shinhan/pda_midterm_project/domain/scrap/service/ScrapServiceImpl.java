// ScrapServiceImpl.java (Implementation)
package com.shinhan.pda_midterm_project.domain.scrap.service;

import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.repository.MemberRepository;
import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;
import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.repository.MemberStockSnapshotRepository;
import com.shinhan.pda_midterm_project.domain.scrap.model.Scrap;
import com.shinhan.pda_midterm_project.domain.scrap.repository.ScrapRepository;
import com.shinhan.pda_midterm_project.domain.scrap_grouped.repository.ScrapGroupedRepository;
import com.shinhan.pda_midterm_project.presentation.scrap.dto.ScrapResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScrapServiceImpl implements ScrapService {

    private final ScrapRepository scrapRepository;
    private final MemberRepository memberRepository;
    private final MemberStockSnapshotRepository memberStockSnapshotRepository;
    private final ScrapGroupedRepository scrapGroupedRepository;

    @Transactional
    @Override
    public ScrapResponseDto createScrap(Long memberId, Long snapshotId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        MemberStockSnapshot snapshot = memberStockSnapshotRepository.findById(snapshotId)
                .orElseThrow(() -> new EntityNotFoundException("Snapshot not found"));

        Scrap newScrap = Scrap.create(member, snapshot);
        Scrap savedScrap = scrapRepository.save(newScrap);

        return new ScrapResponseDto(savedScrap);
    }

    @Transactional
    @Override
    public void deleteScrap(Long memberId, Long scrapId) {
        Scrap scrapToDelete = scrapRepository.findById(scrapId)
                .orElseThrow(() -> new EntityNotFoundException("Scrap not found"));

        if (!scrapToDelete.getMember().getId().equals(memberId)) {
            throw new SecurityException("You do not have permission to delete this scrap.");
        }

        // 1. 이 스크랩을 참조하는 모든 그룹 매핑(ScrapGrouped)을 먼저 삭제합니다.
        scrapGroupedRepository.deleteAllByScrap(scrapToDelete);

        // 2. 원본 스크랩을 삭제합니다.
        scrapRepository.delete(scrapToDelete);
    }
}