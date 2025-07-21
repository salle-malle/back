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
import com.shinhan.pda_midterm_project.presentation.kis.dto.request.KisPresentBalanceRequest;
import com.shinhan.pda_midterm_project.presentation.kis.dto.response.KisPresentBalanceResponse;

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

        // 2. 일반주식 잔고 조회
        List<KisBalanceResponse.KisBalanceItem> regularBalanceItems = getRegularBalanceItems(member, accessToken);

        // 3. 소수점 주식 잔고 조회
        List<KisPresentBalanceResponse.KisPresentBalanceItem> fractionalBalanceItems = getFractionalBalanceItems(member,
                accessToken);

        // 4. 일반주식에서 종목 정보 추출 및 Stock 엔티티 저장
        for (KisBalanceResponse.KisBalanceItem item : regularBalanceItems) {
            saveStockFromBalanceItem(item);
        }

        // 5. 소수점 주식에서 종목 정보 추출 및 Stock 엔티티 저장
        for (KisPresentBalanceResponse.KisPresentBalanceItem item : fractionalBalanceItems) {
            saveStockFromPresentBalanceItem(item);
        }

        // 6. 일반주식 MemberStock 엔티티 생성 및 저장
        for (KisBalanceResponse.KisBalanceItem item : regularBalanceItems) {
            saveMemberStockFromBalanceItem(member, item);
        }

        // 7. 소수점 주식 MemberStock 엔티티 생성 및 저장
        for (KisPresentBalanceResponse.KisPresentBalanceItem item : fractionalBalanceItems) {
            saveMemberStockFromPresentBalanceItem(member, item);
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

    private List<KisBalanceResponse.KisBalanceItem> getRegularBalanceItems(Member member, String accessToken) {
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

            KisBalanceResponse response = koreaInvestmentService.getBalance(request, accessToken,
                    member.getMemberAppKey(), member.getMemberAppSecret());

            if (response.getOutput1() != null) {
                allItems.addAll(response.getOutput1());
            }
        } catch (Exception e) {
            System.err.println("Error fetching regular balance for exchange: " + exchange + ", currency: " + currency);
            e.printStackTrace();
        }

        return allItems;
    }

    private List<KisPresentBalanceResponse.KisPresentBalanceItem> getFractionalBalanceItems(Member member,
            String accessToken) {
        List<KisPresentBalanceResponse.KisPresentBalanceItem> allItems = new ArrayList<>();

        try {
            KisPresentBalanceRequest request = new KisPresentBalanceRequest();
            request.setCANO(member.getMemberAccountNumber());
            request.setACNT_PRDT_CD("01"); // 계좌상품코드
            request.setWCRC_FRCR_DVSN_CD("02"); // 외화잔고구분코드
            request.setNATN_CD("000"); // 국가코드
            request.setTR_MKET_CD("00"); // 거래시장코드
            request.setINQR_DVSN_CD("00"); // 조회구분코드

            KisPresentBalanceResponse response = koreaInvestmentService.getPresentBalance(request, accessToken,
                    member.getMemberAppKey(), member.getMemberAppSecret());

            if (response.getOutput1() != null) {
                allItems.addAll(response.getOutput1());
            }
        } catch (Exception e) {
            System.err.println("Error fetching fractional balance for member: " + member.getMemberId());
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
                    .stockName(item.getOvrsPdno()) // stockName은 종목코드로 설정 (영문명은 CSV에서 설정됨)
                    // 기본 주식 정보
                    .rsym(item.getOvrsPdno()) // 종목 심볼을 종목코드로 설정
                    .curr(item.getTrCrcyCd()) // 통화
                    // 해외주식 관련 필드들
                    .ovrsPdno(item.getOvrsPdno())
                    .ovrsItemName(item.getOvrsItemName()) // 한글명은 ovrsItemName에 저장
                    .ovrsExcgCd(item.getOvrsExcgCd())
                    .trCrcyCd(item.getTrCrcyCd())
                    .stockIsDelisted(false)
                    .build();

            stockRepository.save(stock);
        }
    }

    private void saveStockFromPresentBalanceItem(KisPresentBalanceResponse.KisPresentBalanceItem item) {
        // Stock 엔티티가 이미 존재하는지 확인 (소수점 주식은 pdno를 사용)
        Optional<Stock> existingStock = stockRepository.findByOvrsPdno(item.getPdno());

        if (existingStock.isEmpty()) {
            // 새로운 Stock 엔티티 생성 (소수점 주식 정보)
            Stock stock = Stock.builder()
                    .stockId(item.getPdno())
                    .stockName(item.getPdno()) // stockName은 종목코드로 설정 (영문명은 CSV에서 설정됨)
                    // 기본 주식 정보
                    .rsym(item.getPdno()) // 종목 심볼을 종목코드로 설정
                    .curr(item.getBuyCrcyCd()) // 통화
                    // 해외주식 관련 필드들 (소수점 주식의 경우 일부 필드가 다를 수 있음)
                    .ovrsPdno(item.getPdno())
                    .ovrsItemName(item.getPrdtName()) // 한글명은 ovrsItemName에 저장
                    .ovrsExcgCd(item.getOvrsExcgCd() != null ? item.getOvrsExcgCd() : "NASD") // 기본값 설정
                    .trCrcyCd(item.getBuyCrcyCd())
                    .stockIsDelisted(false)
                    .build();

            stockRepository.save(stock);
        }
    }

    private void saveMemberStockFromBalanceItem(Member member, KisBalanceResponse.KisBalanceItem item) {
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

    private void saveMemberStockFromPresentBalanceItem(Member member,
            KisPresentBalanceResponse.KisPresentBalanceItem item) {
        // Stock 엔티티 조회 (소수점 주식은 pdno를 사용)
        Optional<Stock> stock = stockRepository.findByOvrsPdno(item.getPdno());

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

    @Transactional
    public void updateNickname(Long memberId, String newNickname) {
        Member member = findById(memberId);
        member.updateProfile(newNickname, member.getMemberPhone());
    }
}
