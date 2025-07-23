package com.shinhan.pda_midterm_project.domain.earning_call.service;

import com.shinhan.pda_midterm_project.domain.earning_call.model.EarningCall;
import com.shinhan.pda_midterm_project.domain.earning_call.repository.EarningCallRepository;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.service.MemberService;
import com.shinhan.pda_midterm_project.domain.member_stock.model.MemberStock;
import com.shinhan.pda_midterm_project.domain.member_stock.service.MemberStockService;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import com.shinhan.pda_midterm_project.domain.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EarningCallServiceImpl implements EarningCallService {

    private final EarningCallRepository earningCallRepository;
    private final StockRepository stockRepository;
    private final MemberService memberService;
    private final MemberStockService memberStockService;
    private final Clock clock;

    @Override
    @Transactional(readOnly = true)
    public List<EarningCall> getAllEarningCalls() {
        return earningCallRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EarningCall> getEarningCallsByStockId(String stockId) {
        return earningCallRepository.findByStockStockId(stockId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EarningCall> getEarningCallsByDate(String date) {
        return earningCallRepository.findByEarningCallDate(date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EarningCall> getEarningCallsByMemberId(Long memberId) {
        List<String> stockIds = getMemberStockIds(memberId);
        return earningCallRepository.findByStockIds(stockIds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EarningCall> getUpcomingEarningCall(Long memberId) {
        List<EarningCall> earningCalls = earningCallRepository.findByMemberId(memberId);
        return earningCalls.stream()
                .filter(ec -> !LocalDate.parse(ec.getEarningCallDate()).isBefore(LocalDate.now(clock)))
                .sorted(Comparator.comparing(ec -> LocalDate.parse(ec.getEarningCallDate())))
                .toList();
    }

    @Override
    @Transactional
    public void createEarningCallsFromCsvForMemberStocks(Long memberId) {
        log.info("회원 ID: {} 의 보유주식에 대한 어닝콜 CSV 파싱 시작", memberId);

        try {
            List<String> memberStockSymbols = getMemberStockIds(memberId);

            if (memberStockSymbols.isEmpty()) {
                log.info("회원 ID: {} 의 보유주식이 없습니다.", memberId);
                return;
            }

            log.info("회원 ID: {} 의 보유주식 심볼: {}", memberId, memberStockSymbols);

            // CSV 파일에서 어닝콜 데이터 파싱
            List<EarningCall> newEarningCalls = parseEarningCallsFromCsvForStocks(memberStockSymbols);

            if (!newEarningCalls.isEmpty()) {
                earningCallRepository.saveAll(newEarningCalls);
                log.info("회원 ID: {} 의 보유주식에 대해 {} 개의 어닝콜을 생성했습니다.", memberId, newEarningCalls.size());
            } else {
                log.info("회원 ID: {} 의 보유주식에 대해 생성할 새로운 어닝콜이 없습니다.", memberId);
            }

        } catch (Exception e) {
            log.error("회원 ID: {} 의 보유주식 어닝콜 생성 중 오류 발생: {}", memberId, e.getMessage(), e);
            throw new RuntimeException("어닝콜 생성 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 회원의 보유주식 ID 목록을 조회하는 공통 메서드
     */
    private List<String> getMemberStockIds(Long memberId) {
        Member member = memberService.findById(memberId);
        List<MemberStock> memberStocks = memberStockService.getMemberStocks(member);
        return memberStocks.stream()
                .map(ms -> ms.getStock().getStockId())
                .collect(Collectors.toList());
    }

    /**
     * CSV 파일에서 특정 주식들의 어닝콜 데이터를 파싱
     */
    private List<EarningCall> parseEarningCallsFromCsvForStocks(List<String> stockSymbols) {
        List<EarningCall> earningCalls = new ArrayList<>();

        try {
            String csvFilePath = "data/earnings_calendar_3month.csv";

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new java.io.FileInputStream(csvFilePath), StandardCharsets.UTF_8))) {

                String line;
                boolean isFirstLine = true;

                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue; // 헤더 스킵
                    }

                    String[] columns = line.split(",");
                    if (columns.length >= 3) {
                        String csvSymbol = columns[0].trim();
                        String reportDate = columns[2].trim();

                        // 보유주식 목록에 포함된 심볼인지 확인 (슬래시를 하이픈으로 변환하여 매칭)
                        String normalizedCsvSymbol = csvSymbol.replace("/", "-");
                        String matchingStockId = stockSymbols.stream()
                                .filter(stockId -> stockId.replace("/", "-").equals(normalizedCsvSymbol))
                                .findFirst()
                                .orElse(null);

                        if (matchingStockId != null) {
                            // 이미 해당 주식의 어닝콜이 존재하는지 확인
                            List<EarningCall> existingEarningCalls = earningCallRepository
                                    .findByStockStockId(matchingStockId);

                            if (existingEarningCalls.isEmpty()) {
                                // 주식 정보 조회
                                Optional<Stock> stock = stockRepository.findById(matchingStockId);

                                if (stock.isPresent()) {
                                    EarningCall earningCall = EarningCall.builder()
                                            .stock(stock.get())
                                            .earningCallDate(reportDate)
                                            .build();

                                    earningCalls.add(earningCall);
                                    log.info("주식 ID: {} (CSV: {}) 에 대한 어닝콜 생성 - 날짜: {}", matchingStockId, csvSymbol,
                                            reportDate);
                                } else {
                                    log.info("주식 ID: {} 의 정보가 데이터베이스에 없습니다.", matchingStockId);
                                }
                            } else {
                                log.info("주식 ID: {} 의 어닝콜이 이미 존재합니다.", matchingStockId);
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            log.error("CSV 파일 파싱 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("CSV 파일 파싱 중 오류가 발생했습니다.", e);
        }

        return earningCalls;
    }
}
