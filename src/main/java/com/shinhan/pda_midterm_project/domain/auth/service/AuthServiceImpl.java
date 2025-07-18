package com.shinhan.pda_midterm_project.domain.auth.service;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.exception.AuthException;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final KoreaInvestmentService koreaInvestmentService;

    @Override
    public ResponseCookie login(String id, String password) {
        Member member = memberService.findByMemberId(id);
        String findMemberId = member.getMemberId();
        String findPassword = member.getMemberPassword();

        if (!id.equals(findMemberId)) {
            throw new AuthException(ResponseMessages.LOGIN_FAIL);
        }
        if (!BCrypt.checkpw(password, findPassword)) {
            throw new AuthException(ResponseMessages.LOGIN_FAIL);
        }
        String accessToken = tokenProvider.generateTokens(member.getId().toString());
        return TokenCookieManager.createCookie(accessToken);
    }

    @Override
    public ResponseCookie signUp(String id, String password, String phoneNumber, String appKey, String appSecret,
                                 String accountNumber) {
        String hashedPw = BCrypt.hashpw(password, BCrypt.gensalt());
        Member member = Member.create(id, hashedPw, phoneNumber);

        // KIS 관련 정보 설정
        member.setKisInfo(appKey, appSecret, accountNumber);

        try {
            var tokenResponse = koreaInvestmentService.getAccessToken(
                    member.getMemberAppKey(),
                    member.getMemberAppSecret());
            String accessToken = tokenResponse.getAccessToken();

            if (accessToken == null || accessToken.isEmpty()) {
                throw new RuntimeException("Failed to get access token from KIS API");
            }

            // 토큰을 Member 엔티티에 저장
            member.updateKisAccessToken(accessToken);
            memberService.saveMember(member);

        } catch (Exception e) {
            System.err.println("Error getting KIS access token: " + e.getMessage());
            e.printStackTrace();
            // 토큰 발급 실패 시에도 회원은 저장
            memberService.saveMember(member);
            throw new RuntimeException("Failed to get KIS access token during signup", e);
        }

        // DB에 저장된 member를 다시 조회하여 토큰이 포함된 상태로 가져옴
        Member savedMember = memberService.findById(member.getId());

        // 회원가입 후 해외주식 잔고 조회 및 저장
        try {
            memberService.fetchAndSaveMemberStocks(savedMember);
        } catch (Exception e) {
            System.err.println("Error fetching member stocks during signup: " + e.getMessage());
            // 회원가입은 성공하지만 주식 조회는 실패할 수 있음
        }
        String accessToken = tokenProvider.generateTokens(member.getId().toString());
        return TokenCookieManager.createCookie(accessToken);
    }
}