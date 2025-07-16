package com.shinhan.pda_midterm_project.domain.auth.service;

import org.springframework.stereotype.Component;

@Component
public class TokenExpirationChecker {

  /**
   * 토큰이 유효한지 확인
   * 실제 구현에서는 JWT 토큰의 만료시간을 체크해야 함
   */
  public boolean isTokenValid(String token) {
    if (token == null || token.isEmpty()) {
      return false;
    }

    // TODO: 실제 토큰 만료 체크 로직 구현
    // 현재는 단순히 토큰이 존재하는지만 확인
    return true;
  }
}