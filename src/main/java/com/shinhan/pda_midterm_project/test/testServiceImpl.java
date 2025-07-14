package com.shinhan.pda_midterm_project.test;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.member.exception.MemberException;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class testServiceImpl implements testService {

    private final MemberRepository memberRepository;

    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ResponseMessages.MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void updatePhoneNumber(Long memberId, String phoneNumber) {
        Member member = findById(memberId);

        member.updateProfile(
                member.getMemberId(),
                member.getMemberNickname()
//                phoneNumber
        );
    }

}
