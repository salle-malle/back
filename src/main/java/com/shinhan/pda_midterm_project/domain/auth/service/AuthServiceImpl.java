package com.shinhan.pda_midterm_project.domain.auth.service;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.exception.AuthException;
import com.shinhan.pda_midterm_project.domain.earning_call.service.EarningCallService;
import com.shinhan.pda_midterm_project.domain.member.exception.MemberException;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.service.MemberService;
import com.shinhan.pda_midterm_project.domain.member_stock.service.MemberStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final KoreaInvestmentService koreaInvestmentService;
    private final EarningCallService earningCallService;
    private final MemberStockService memberStockService;

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
    public ResponseCookie signUp(String name, String nickname, String id, String password, String phoneNumber,
            String appKey, String appSecret,
            String accountNumber) {
        String hashedPw = BCrypt.hashpw(password, BCrypt.gensalt());
        Member member = Member.create(id, name, hashedPw, phoneNumber, nickname, accountNumber, appKey,
                appSecret);

        // KIS 관련 정보 설정
        member.setKisInfo(appKey, appSecret, accountNumber);

        var tokenResponse = koreaInvestmentService.getAccessToken(
                member.getMemberAppKey(),
                member.getMemberAppSecret());
        String kisAccessToken = tokenResponse.getAccessToken();

        if (kisAccessToken == null || kisAccessToken.isEmpty()) {
            throw new MemberException(ResponseMessages.INVALID_ACCOUNT_INFO);
        }

        // 토큰을 Member 엔티티에 저장
        member.updateKisAccessToken(kisAccessToken);
        memberService.saveMember(member);

        // DB에 저장된 member를 다시 조회하여 토큰이 포함된 상태로 가져옴
        Member savedMember = memberService.findById(member.getId());

        // 1. 회원가입 후 해외주식 잔고 조회 및 저장
        try {
            memberService.fetchAndSaveMemberStocks(savedMember);
            log.info("회원 ID: {} 의 보유주식 조회 및 저장 완료", savedMember.getId());
        } catch (Exception e) {
            log.error("회원가입 중 보유주식 조회 실패: {}", e.getMessage());
            // 회원가입은 성공하지만 주식 조회는 실패할 수 있음
        }

        // 2. 모든 주식의 상세정보 갱신
        try {
            memberStockService.refreshAllMemberStockDetails(savedMember);
            log.info("회원 ID: {} 의 주식 상세정보 갱신 완료", savedMember.getId());
        } catch (Exception e) {
            log.error("회원가입 중 주식 상세정보 갱신 실패: {}", e.getMessage());
            // 주식 상세정보 갱신 실패는 전체 프로세스를 중단하지 않음
        }

        // 3. 보유주식에 대한 어닝콜 데이터 생성
        try {
            earningCallService.createEarningCallsFromCsvForMemberStocks(savedMember.getId());
            log.info("회원 ID: {} 의 보유주식에 대한 어닝콜 데이터 생성 완료", savedMember.getId());
        } catch (Exception e) {
            log.error("회원가입 중 어닝콜 데이터 생성 실패: {}", e.getMessage());
            // 어닝콜 생성 실패는 전체 프로세스를 중단하지 않음
        }

        String accessToken = tokenProvider.generateTokens(member.getId().toString());
        return TokenCookieManager.createCookie(accessToken);
    }
}
