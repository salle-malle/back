package com.shinhan.pda_midterm_project.presentation.kis.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KisStockDetailResponse {

  @JsonProperty("output")
  private KisStockDetailOutput output;

  @JsonProperty("rt_cd")
  private String rtCd; // 응답 코드

  @JsonProperty("msg_cd")
  private String msgCd; // 메시지 코드

  @JsonProperty("msg1")
  private String msg1; // 메시지

  @Getter
  @NoArgsConstructor
  public static class KisStockDetailOutput {
    @JsonProperty("rsym")
    private String rsym; // 종목 심볼

    @JsonProperty("zdiv")
    private String zdiv; // 구분

    @JsonProperty("curr")
    private String curr; // 통화

    @JsonProperty("vnit")
    private String vnit; // 단위

    @JsonProperty("open")
    private String open; // 시가

    @JsonProperty("high")
    private String high; // 고가

    @JsonProperty("low")
    private String low; // 저가

    @JsonProperty("last")
    private String last; // 현재가

    @JsonProperty("base")
    private String base; // 기준가

    @JsonProperty("pvol")
    private String pvol; // 전일 거래량

    @JsonProperty("pamt")
    private String pamt; // 전일 거래대금

    @JsonProperty("uplp")
    private String uplp; // 상한가

    @JsonProperty("dnlp")
    private String dnlp; // 하한가

    @JsonProperty("h52p")
    private String h52p; // 52주 최고가

    @JsonProperty("h52d")
    private String h52d; // 52주 최고가 날짜

    @JsonProperty("l52p")
    private String l52p; // 52주 최저가

    @JsonProperty("l52d")
    private String l52d; // 52주 최저가 날짜

    @JsonProperty("perx")
    private String perx; // PER

    @JsonProperty("pbrx")
    private String pbrx; // PBR

    @JsonProperty("epsx")
    private String epsx; // EPS

    @JsonProperty("bpsx")
    private String bpsx; // BPS

    @JsonProperty("shar")
    private String shar; // 상장주식수

    @JsonProperty("mcap")
    private String mcap; // 시가총액

    @JsonProperty("tomv")
    private String tomv; // 거래대금

    @JsonProperty("t_xprc")
    private String tXprc; // 현재가

    @JsonProperty("t_xdif")
    private String tXdif; // 등락

    @JsonProperty("t_xrat")
    private String tXrat; // 등락률

    @JsonProperty("p_xprc")
    private String pXprc; // 전일 현재가

    @JsonProperty("p_xdif")
    private String pXdif; // 전일 등락

    @JsonProperty("p_xrat")
    private String pXrat; // 전일 등락률

    @JsonProperty("t_rate")
    private String tRate; // 현재 등락률

    @JsonProperty("p_rate")
    private String pRate; // 전일 등락률

    @JsonProperty("t_xsgn")
    private String tXsgn; // 현재 등락 부호

    @JsonProperty("p_xsng")
    private String pXsng; // 전일 등락 부호

    @JsonProperty("e_ordyn")
    private String eOrdyn; // 매매가능여부

    @JsonProperty("e_hogau")
    private String eHogau; // 호가단위

    @JsonProperty("e_icod")
    private String eIcod; // 업종

    @JsonProperty("e_parp")
    private String eParp; // 파생상품

    @JsonProperty("tvol")
    private String tvol; // 거래량

    @JsonProperty("tamt")
    private String tamt; // 거래대금

    @JsonProperty("etyp_nm")
    private String etypNm; // 종목명
  }
}