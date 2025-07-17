package com.shinhan.pda_midterm_project.domain.total_summary.service;

import com.shinhan.pda_midterm_project.domain.investment_type_news_comment.model.InvestmentTypeNewsComment;
import com.shinhan.pda_midterm_project.domain.investment_type_news_comment.repository.InvestmentTypeNewsCommentRepository;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.repository.MemberRepository;
import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;
import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.repository.MemberStockSnapshotRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TotalSummaryService {

    private final MemberRepository memberRepository;
    private final MemberStockSnapshotRepository snapshotRepository;
    private final InvestmentTypeNewsCommentRepository investmentTypeNewsCommentRepository;

    public void printAllMemberIds() {
        List<Member> members = memberRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Member member : members) {
            Long memberId = member.getId();
            List<MemberStockSnapshot> snapshots = snapshotRepository.findAllByMemberId(memberId);

            List<MemberStockSnapshot> todaySnapshots = snapshots.stream()
                    .filter(snapshot -> snapshot.getCreatedAt().toLocalDate().isEqual(today))
                    .toList();

            StringBuilder mergedSummary = new StringBuilder();

            for (MemberStockSnapshot snapshot : todaySnapshots) {
                InvestmentTypeNewsComment comment = snapshot.getInvestmentTypeNewsComment();
                mergedSummary.append(comment.getInvestmentTypeNewsContent()).append("\n");

                log.info("✅ Member ID: {}, Comment ID: {}", memberId, comment.getId());
                log.info("📄 요약 내용: {}", comment.getInvestmentTypeNewsContent()); 
            }
        }
    }
}
