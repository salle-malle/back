package com.shinhan.pda_midterm_project.domain.stock.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shinhan.pda_midterm_project.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Stock extends BaseEntity {
  @Id
  @Column(length = 255)
  private String stockId;

  @Column(nullable = false, length = 255)
  private String stockName;

  // 기본 주식 정보
  @Column(length = 20)
  private String rsym; // 종목 심볼

  @Column(length = 10)
  private String zdiv; // 구분 코드

  @Column(length = 3)
  private String curr; // 거래 통화

  @Column(length = 10)
  private String vnit; // 가격 단위

  // 가격 정보
  @Column(precision = 19, scale = 4)
  private BigDecimal open; // 시가

  @Column(precision = 19, scale = 4)
  private BigDecimal high; // 고가

  @Column(precision = 19, scale = 4)
  private BigDecimal low; // 저가

  @Column(precision = 19, scale = 4)
  private BigDecimal last; // 현재가

  @Column(precision = 19, scale = 4)
  private BigDecimal base; // 기준가

  // 거래량/거래대금
  @Column
  private Long pvol; // 전일 거래량

  @Column
  private Long pamt; // 전일 거래대금

  @Column
  private Long tvol; // 현재 거래량

  @Column
  private Long tamt; // 현재 거래대금

  // 가격 제한
  @Column(precision = 19, scale = 4)
  private BigDecimal uplp; // 상한가

  @Column(precision = 19, scale = 4)
  private BigDecimal dnlp; // 하한가

  // 52주 정보
  @Column(precision = 19, scale = 4)
  private BigDecimal h52p; // 52주 최고가

  @Column(length = 8)
  private String h52d; // 52주 최고가 날짜

  @Column(precision = 19, scale = 4)
  private BigDecimal l52p; // 52주 최저가

  @Column(length = 8)
  private String l52d; // 52주 최저가 날짜

  // 재무 지표
  @Column(precision = 19, scale = 4)
  private BigDecimal perx; // PER

  @Column(precision = 19, scale = 4)
  private BigDecimal pbrx; // PBR

  @Column(precision = 19, scale = 4)
  private BigDecimal epsx; // EPS

  @Column(precision = 19, scale = 4)
  private BigDecimal bpsx; // BPS

  // 시장 정보
  @Column
  private Long shar; // 상장주식수

  @Column
  private Long mcap; // 시가총액

  @Column
  private Long tomv; // 거래대금

  // 현재가 등락 정보
  @Column(precision = 19, scale = 4)
  private BigDecimal tXprc; // 현재가

  @Column(precision = 19, scale = 4)
  private BigDecimal tXdif; // 등락

  @Column(precision = 19, scale = 4)
  private BigDecimal tXrat; // 등락률

  @Column(precision = 19, scale = 4)
  private BigDecimal tRate; // 현재 등락률

  @Column(length = 10)
  private String tXsgn; // 현재 등락 부호

  // 전일 등락 정보
  @Column(precision = 19, scale = 4)
  private BigDecimal pXprc; // 전일 현재가

  @Column(precision = 19, scale = 4)
  private BigDecimal pXdif; // 전일 등락

  @Column(precision = 19, scale = 4)
  private BigDecimal pXrat; // 전일 등락률

  @Column(precision = 19, scale = 4)
  private BigDecimal pRate; // 전일 등락률

  @Column(length = 10)
  private String pXsng; // 전일 등락 부호

  // 거래 정보
  @Column(length = 50)
  private String eOrdyn; // 매매가능여부

  @Column(precision = 19, scale = 4)
  private BigDecimal eHogau; // 호가단위

  @Column(length = 50)
  private String eIcod; // 업종

  @Column(precision = 19, scale = 4)
  private BigDecimal eParp; // 파생상품

  @Column(length = 100)
  private String etypNm; // 종목명

  @Column(columnDefinition = "TEXT")
  private String stockImageUri; // 주식 이미지 URI

  @Column
  private Boolean stockIsDelisted; // 상장폐지 여부

  // 해외주식 관련 필드들
  @Column(length = 12)
  private String ovrsPdno; // 해외상품번호 (종목코드)

  @Column(length = 60)
  private String ovrsItemName; // 해외종목명

  @Column(length = 4)
  private String ovrsExcgCd; // 해외거래소코드 (NASD, NYSE, AMEX, SEHK, SHAA, SZAA, TKSE, HASE, VNSE)

  @Column(length = 3)
  private String trCrcyCd; // 거래통화코드 (USD, HKD, CNY, JPY, VND)

  @Column(length = 3)
  private String prdtTypeCd; // 상품유형코드
}