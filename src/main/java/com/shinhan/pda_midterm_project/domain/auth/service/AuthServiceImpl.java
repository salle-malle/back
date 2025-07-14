package com.shinhan.pda_midterm_project.domain.auth.service;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.exception.AuthException;
import com.shinhan.pda_midterm_project.domain.auth.model.UserTokens;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @Override
    public UserTokens login(String id, String password) {
        Member member = memberService.findByMemberId(id);
        String findMemberId = member.getMemberId();
        String findPassword = member.getMemberPassword();

        if (!id.equals(findMemberId)) {
            throw new AuthException(ResponseMessages.LOGIN_FAIL);
        }
        if (!BCrypt.checkpw(password, findPassword)) {
            throw new AuthException(ResponseMessages.LOGIN_FAIL);
        }

        return tokenProvider.generateTokens(member.getId().toString());
    }

    @Override
    public UserTokens signUp(String id, String password, String phoneNumber) {
        String hashedPw = BCrypt.hashpw(password, BCrypt.gensalt());
        Member member = Member.create(id, hashedPw, phoneNumber);

        memberService.saveMember(member);

        return tokenProvider.generateTokens(member.getId().toString());
    }
}
