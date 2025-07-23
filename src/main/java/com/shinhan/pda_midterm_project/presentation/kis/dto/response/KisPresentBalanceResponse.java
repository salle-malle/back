package com.shinhan.pda_midterm_project.presentation.kis.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class KisPresentBalanceResponse {

  @JsonProperty("output1")
  private List<KisPresentBalanceItem> output1; // 소수점 잔고내역

  @JsonProperty("output2")
  private List<Object> output2; // 추가 출력 (현재 빈 배열)

  @JsonProperty("output3")
  private KisPresentBalanceSummary output3; // 소수점 잔고요약

  @JsonProperty("rt_cd")
  private String rtCd; // 응답코드

  @JsonProperty("msg_cd")
  private String msgCd; // 메시지코드

  @JsonProperty("msg1")
  private String msg1; // 메시지

  @Getter
  @NoArgsConstructor
  public static class KisPresentBalanceItem {
    @JsonProperty("prdt_name")
    private String prdtName; // 상품명

    @JsonProperty("cblc_qty13")
    private String cblcQty13; // 잔고수량

    @JsonProperty("thdt_buy_ccld_qty1")
    private String thdtBuyCcldQty1; // 당일매수체결수량

    @JsonProperty("thdt_sll_ccld_qty1")
    private String thdtSllCcldQty1; // 당일매도체결수량

    @JsonProperty("ccld_qty_smtl1")
    private String ccldQtySmtl1; // 체결수량합계

    @JsonProperty("ord_psbl_qty1")
    private String ordPsblQty1; // 주문가능수량

    @JsonProperty("frcr_pchs_amt")
    private String frcrPchsAmt; // 매입금액

    @JsonProperty("frcr_evlu_amt2")
    private String frcrEvluAmt2; // 평가금액

    @JsonProperty("evlu_pfls_amt2")
    private String evluPflsAmt2; // 평가손익금액

    @JsonProperty("evlu_pfls_rt1")
    private String evluPflsRt1; // 평가손익률

    @JsonProperty("pdno")
    private String pdno; // 상품번호

    @JsonProperty("bass_exrt")
    private String bassExrt; // 기준환율

    @JsonProperty("buy_crcy_cd")
    private String buyCrcyCd; // 매수통화코드

    @JsonProperty("ovrs_now_pric1")
    private String ovrsNowPric1; // 해외현재가

    @JsonProperty("avg_unpr3")
    private String avgUnpr3; // 평균단가

    @JsonProperty("tr_mket_name")
    private String trMketName; // 거래시장명

    @JsonProperty("natn_kor_name")
    private String natnKorName; // 국가명

    @JsonProperty("pchs_rmnd_wcrc_amt")
    private String pchsRmndWcrcAmt; // 매입잔여원화금액

    @JsonProperty("thdt_buy_ccld_frcr_amt")
    private String thdtBuyCcldFrcrAmt; // 당일매수체결외화금액

    @JsonProperty("thdt_sll_ccld_frcr_amt")
    private String thdtSllCcldFrcrAmt; // 당일매도체결외화금액

    @JsonProperty("unit_amt")
    private String unitAmt; // 단위금액

    @JsonProperty("std_pdno")
    private String stdPdno; // 표준상품번호

    @JsonProperty("prdt_type_cd")
    private String prdtTypeCd; // 상품유형코드

    @JsonProperty("scts_dvsn_name")
    private String sctsDvsnName; // 증권구분명

    @JsonProperty("loan_rmnd")
    private String loanRmnd; // 대출잔고

    @JsonProperty("loan_dt")
    private String loanDt; // 대출일자

    @JsonProperty("loan_expd_dt")
    private String loanExpdDt; // 대출만기일자

    @JsonProperty("ovrs_excg_cd")
    private String ovrsExcgCd; // 해외거래소코드

    @JsonProperty("item_lnkg_excg_cd")
    private String itemLnkgExcgCd; // 종목연결거래소코드

    @JsonProperty("prdt_dvsn")
    private String prdtDvsn; // 상품구분
  }

  @Getter
  @NoArgsConstructor
  public static class KisPresentBalanceSummary {
    @JsonProperty("pchs_amt_smtl")
    private String pchsAmtSmtl; // 매입금액합계

    @JsonProperty("evlu_amt_smtl")
    private String evluAmtSmtl; // 평가금액합계

    @JsonProperty("evlu_pfls_amt_smtl")
    private String evluPflsAmtSmtl; // 평가손익금액합계

    @JsonProperty("dncl_amt")
    private String dnclAmt; // 신용금액

    @JsonProperty("cma_evlu_amt")
    private String cmaEvluAmt; // CMA평가금액

    @JsonProperty("tot_dncl_amt")
    private String totDnclAmt; // 총신용금액

    @JsonProperty("etc_mgna")
    private String etcMgna; // 기타증거금

    @JsonProperty("wdrw_psbl_tot_amt")
    private String wdrwPsblTotAmt; // 출금가능총금액

    @JsonProperty("frcr_evlu_tota")
    private String frcrEvluTota; // 외화평가총액

    @JsonProperty("evlu_erng_rt1")
    private String evluErngRt1; // 평가수익률

    @JsonProperty("pchs_amt_smtl_amt")
    private String pchsAmtSmtlAmt; // 매입금액합계금액

    @JsonProperty("evlu_amt_smtl_amt")
    private String evluAmtSmtlAmt; // 평가금액합계금액

    @JsonProperty("tot_evlu_pfls_amt")
    private String totEvluPflsAmt; // 총평가손익금액

    @JsonProperty("tot_asst_amt")
    private String totAsstAmt; // 총자산금액

    @JsonProperty("buy_mgn_amt")
    private String buyMgnAmt; // 매수증거금

    @JsonProperty("mgna_tota")
    private String mgnaTota; // 증거금총액

    @JsonProperty("frcr_use_psbl_amt")
    private String frcrUsePsblAmt; // 외화사용가능금액

    @JsonProperty("ustl_sll_amt_smtl")
    private String ustlSllAmtSmtl; // 미결제매도금액합계

    @JsonProperty("ustl_buy_amt_smtl")
    private String ustlBuyAmtSmtl; // 미결제매수금액합계

    @JsonProperty("tot_frcr_cblc_smtl")
    private String totFrcrCblcSmtl; // 총외화잔고합계

    @JsonProperty("tot_loan_amt")
    private String totLoanAmt; // 총대출금액
  }
}