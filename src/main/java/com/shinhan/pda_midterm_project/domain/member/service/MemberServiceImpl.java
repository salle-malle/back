package com.shinhan.pda_midterm_project.domain.member.service;

import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.service.KoreaInvestmentService;
import com.shinhan.pda_midterm_project.domain.member.exception.MemberException;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.repository.MemberRepository;
import com.shinhan.pda_midterm_project.domain.member_stock.model.MemberStock;
import com.shinhan.pda_midterm_project.domain.member_stock.repository.MemberStockRepository;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import com.shinhan.pda_midterm_project.domain.stock.repository.StockRepository;
import com.shinhan.pda_midterm_project.presentation.kis.dto.request.KisBalanceRequest;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisBalanceResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final KoreaInvestmentService koreaInvestmentService;
    private final MemberStockRepository memberStockRepository;
    private final StockRepository stockRepository;

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

    @Override
    @Transactional
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void fetchAndSaveMemberStocks(Member member) {
        // 1. KIS 액세스 토큰 발급
        String accessToken = getAccessToken(member);

        // 2. 해외주식 잔고 조회
        List<KisBalanceResponse.KisBalanceItem> balanceItems = getBalanceItems(member, accessToken);

        // 3. 잔고에서 종목 정보 추출 및 Stock 엔티티 저장
        for (KisBalanceResponse.KisBalanceItem item : balanceItems) {
            saveStockFromBalanceItem(item);
        }

        // 4. MemberStock 엔티티 생성 및 저장
        for (KisBalanceResponse.KisBalanceItem item : balanceItems) {
            saveMemberStock(member, item);
        }
    }

    private String getAccessToken(Member member) {
        // DB에서 저장된 KIS 액세스 토큰 사용
        String accessToken = member.getKisAccessToken();

        if (accessToken == null || accessToken.isEmpty()) {
            throw new RuntimeException("KIS access token not found in database for member: " + member.getId());
        }

        return accessToken;
    }

    private List<KisBalanceResponse.KisBalanceItem> getBalanceItems(Member member, String accessToken) {
        List<KisBalanceResponse.KisBalanceItem> allItems = new ArrayList<>();

        // 하나의 거래소만 조회 (NASD - 나스닥)
        String exchange = "NASD";
        String currency = "USD";

        try {
            KisBalanceRequest request = new KisBalanceRequest();
            request.setCANO(member.getMemberAccountNumber());
            request.setACNT_PRDT_CD("01"); // 계좌상품코드 (일반적으로 01)
            request.setOVRS_EXCG_CD(exchange);
            request.setTR_CRCY_CD(currency);

            System.out.println("Fetching balance for exchange: " + exchange + ", currency: " + currency);
            KisBalanceResponse response = koreaInvestmentService.getBalance(request, accessToken,
                    member.getMemberAppKey(), member.getMemberAppSecret());

            if (response.getOutput1() != null) {
                allItems.addAll(response.getOutput1());
            }
        } catch (Exception e) {
            System.err.println("Error fetching balance for exchange: " + exchange + ", currency: " + currency);
            e.printStackTrace();
        }

        return allItems;
    }

    private void saveStockFromBalanceItem(KisBalanceResponse.KisBalanceItem item) {
        // Stock 엔티티가 이미 존재하는지 확인
        Optional<Stock> existingStock = stockRepository.findByOvrsPdno(item.getOvrsPdno());

        if (existingStock.isEmpty()) {
            // 새로운 Stock 엔티티 생성 (기본 정보만)
            Stock stock = Stock.builder()
                    .stockId(item.getOvrsPdno())
                    .stockName(item.getOvrsItemName())
                    // 기본 주식 정보
                    .rsym(item.getOvrsPdno()) // 종목 심볼을 종목코드로 설정
                    .curr(item.getTrCrcyCd()) // 통화
                    // 해외주식 관련 필드들
                    .ovrsPdno(item.getOvrsPdno())
                    .ovrsItemName(item.getOvrsItemName())
                    .ovrsExcgCd(item.getOvrsExcgCd())
                    .trCrcyCd(item.getTrCrcyCd())
                    .stockIsDelisted(false)
                    .build();

            stockRepository.save(stock);
        }
    }

    private void saveMemberStock(Member member, KisBalanceResponse.KisBalanceItem item) {
        // Stock 엔티티 조회
        Optional<Stock> stock = stockRepository.findByOvrsPdno(item.getOvrsPdno());

        if (stock.isPresent()) {
            // MemberStock이 이미 존재하는지 확인
            Optional<MemberStock> existingMemberStock = memberStockRepository.findByMemberAndStock(
                    member, stock.get());

            if (existingMemberStock.isEmpty()) {
                // 새로운 MemberStock 엔티티 생성
                MemberStock memberStock = MemberStock.builder()
                        .member(member)
                        .stock(stock.get())
                        .build();

                memberStockRepository.save(memberStock);
            }
        }
    }
}
