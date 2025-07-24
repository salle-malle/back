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
import com.shinhan.pda_midterm_project.presentation.scrap.dto.ScrapStatusResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Transactional(readOnly = true) // '읽기 전용' 조회이므로 readOnly = true를 붙이는 것이 좋습니다.
    @Override
    public ScrapStatusResponse getScrapStatus(Long memberId, Long snapshotId) {
        // [수정] Scrap의 PK(Id)가 아닌, 연관된 MemberStockSnapshot의 ID로 조회해야 합니다.
        Optional<Scrap> scrapOptional = scrapRepository.findByMemberIdAndMemberStockSnapshotId(memberId, snapshotId);

        if (scrapOptional.isPresent()) {
            // 스크랩이 존재하면 true와 실제 scrap.id를 반환
            return new ScrapStatusResponse(true, scrapOptional.get().getId());
        } else {
            // 스크랩이 없으면 false와 null을 반환
            return new ScrapStatusResponse(false, null);
        }
    }

    @Transactional
    @Override
    public void deleteScrapBySnapshotId(Long memberId, Long snapshotId) {
        Scrap scrapToDelete = scrapRepository.findByMemberIdAndMemberStockSnapshotId(memberId, snapshotId)
                .orElse(null);

        if (scrapToDelete != null) {
            scrapGroupedRepository.deleteAllByScrap(scrapToDelete);
            scrapRepository.delete(scrapToDelete);
        }
    }
}