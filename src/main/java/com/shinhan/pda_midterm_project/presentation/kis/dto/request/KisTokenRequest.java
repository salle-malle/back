package com.shinhan.pda_midterm_project.presentation.kis.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KisTokenRequest {
  private String appkey;
  private String appsecret;
}