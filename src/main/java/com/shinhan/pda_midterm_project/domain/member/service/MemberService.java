package com.shinhan.pda_midterm_project.domain.member.service;

import com.shinhan.pda_midterm_project.domain.member.model.Member;

public interface MemberService {
    Member findById(Long memberId);

    void updatePhoneNumber(Long memberId, String phoneNumber);
}
