package com.shinhan.pda_midterm_project.domain.member.service;

import com.shinhan.pda_midterm_project.domain.investment_type.model.InvestmentType;
import com.shinhan.pda_midterm_project.domain.investment_type.repository.InvestmentTypeRepository;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.repository.MemberRepository;
import com.shinhan.pda_midterm_project.presentation.member.dto.response.MyPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;
    private final InvestmentTypeRepository investmentTypeRepository;

    public MyPageResponse getMyPageInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        return new MyPageResponse(
                member.getMemberNickname(),
                member.getInvestmentType() != null
                        ? member.getInvestmentType().getInvestmentName()
                        : null
        );
    }

    @Transactional
    public void updateInvestmentType(Long memberId, Long investmentTypeId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        InvestmentType investmentType = investmentTypeRepository.findById(investmentTypeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 투자 성향입니다."));

        member.updateInvestmentType(investmentType);
    }
}
