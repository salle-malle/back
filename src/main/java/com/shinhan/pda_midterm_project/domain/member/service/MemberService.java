package com.shinhan.pda_midterm_project.domain.member.service;

import com.shinhan.pda_midterm_project.domain.member.model.Member;

public interface MemberService {
    Member findById(Long id);

    void updatePhoneNumber(Long memberId, String phoneNumber);

    Member findByMemberId(String memberId);

    void saveMember(Member member);

    /**
     * 회원가입 후 해외주식 잔고 조회 및 저장
     */
    void fetchAndSaveMemberStocks(Member member);

    void updateNickname(Long aLong, String nickname);

    void checkMemberIdDuplicated(String memberId);
}
