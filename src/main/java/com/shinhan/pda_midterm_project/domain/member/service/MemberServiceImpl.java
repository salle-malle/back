package com.shinhan.pda_midterm_project.domain.member.service;

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
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;

  @Override
  public Member findById(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new MemberException(ResponseMessages.MEMBER_NOT_FOUND));
  }

  @Override
  @Transactional
  public void updatePhoneNumber(Long memberId, String phoneNumber) {
    Member member = findById(memberId);

    member.updateProfile(
        member.getMemberNickname(),
        phoneNumber);
  }

  @Override
  public Member findByMemberId(String memberId) {
    return memberRepository.findByMemberId(memberId)
            .orElseThrow(() -> new MemberException(ResponseMessages.MEMBER_NOT_FOUND));
  }
}
