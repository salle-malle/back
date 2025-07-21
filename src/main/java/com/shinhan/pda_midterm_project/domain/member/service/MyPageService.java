package com.shinhan.pda_midterm_project.domain.member.service;

import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.repository.MemberRepository;
import com.shinhan.pda_midterm_project.presentation.member.dto.response.MyPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;

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
}
