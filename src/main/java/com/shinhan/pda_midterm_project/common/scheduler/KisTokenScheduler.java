package com.shinhan.pda_midterm_project.common.scheduler;

import com.shinhan.pda_midterm_project.domain.auth.service.KoreaInvestmentService;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.repository.MemberRepository;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KisTokenScheduler {

  private final KoreaInvestmentService koreaInvestmentService;
  private final MemberRepository memberRepository;

  /**
   * 매분마다 모든 회원의 KIS 토큰을 갱신
   */
  @Scheduled(cron = "0 0 6 * * *")
  // @Scheduled(cron = "0 * * * * *")
  public void refreshKisTokens() {
    log.info("KIS Token Refresh Scheduler 시작");

    try {
      List<Member> members = memberRepository.findAll();

      for (Member member : members) {
        try {
          if (member.getMemberAppKey() != null && member.getMemberAppSecret() != null) {
            log.info("회원 ID: {} 토큰 갱신 시작", member.getId());

            // 토큰 갱신
            KisTokenResponse tokenResponse = koreaInvestmentService.getAccessToken(
                member.getMemberAppKey(),
                member.getMemberAppSecret());
            String newToken = tokenResponse.getAccessToken();

            // 새로운 토큰으로 업데이트
            member.updateKisAccessToken(newToken);
            memberRepository.save(member);

            log.info("회원 ID: {} 토큰 갱신 완료", member.getId());
          }
        } catch (Exception e) {
          log.error("회원 ID: {} 토큰 갱신 실패: {}", member.getId(), e.getMessage());
        }
      }

      log.info("KIS Token Refresh Scheduler 완료");
    } catch (Exception e) {
      log.error("KIS Token Refresh Scheduler 실행 중 오류 발생: {}", e.getMessage());
    }
  }

  /**
   * 수동으로 토큰 갱신을 실행하는 메서드
   */
  public void refreshKisTokensManually() {
    log.info("수동 KIS Token Refresh 실행");
    refreshKisTokens();
  }
}