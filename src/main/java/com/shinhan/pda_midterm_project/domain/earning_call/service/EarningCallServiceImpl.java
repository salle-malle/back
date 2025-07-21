package com.shinhan.pda_midterm_project.domain.earning_call.service;

import com.shinhan.pda_midterm_project.domain.earning_call.model.EarningCall;
import com.shinhan.pda_midterm_project.domain.earning_call.repository.EarningCallRepository;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.service.MemberService;
import com.shinhan.pda_midterm_project.domain.member_stock.model.MemberStock;
import com.shinhan.pda_midterm_project.domain.member_stock.service.MemberStockService;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import com.shinhan.pda_midterm_project.domain.stock.repository.StockRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    public void parseAndSaveEarningCalls(MultipartFile csvFile) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean isFirstLine = true;
            List<EarningCall> earningCalls = new ArrayList<>();
            int processedCount = 0;
            int savedCount = 0;
            int stockCreatedCount = 0;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // 헤더 스킵
                }

                String[] columns = line.split(",");
                if (columns.length >= 3) {
                    String symbol = columns[0].trim();
                    String stockName = columns[1].trim(); // stockName은 2번째 컬럼
                    String reportDate = columns[2].trim(); // reportDate는 3번째 컬럼
                    processedCount++;

                    // 주식 정보 조회 또는 생성
                    Stock stock = getOrCreateStock(symbol, stockName);
                    if (stock != null) {
                        EarningCall earningCall = EarningCall.builder()
                                .stock(stock)
                                .earningCallDate(reportDate)
                                .build();

                        earningCalls.add(earningCall);
                        savedCount++;
                    }
                }
            }

            // 배치로 저장
            if (!earningCalls.isEmpty()) {
                earningCallRepository.saveAll(earningCalls);
                log.info("Processed {} records, saved {} earning call records, created {} stocks",
                        processedCount, savedCount, stockCreatedCount);
            } else {
                log.warn("No earning calls to save. Processed {} records but no valid data found.", processedCount);
            }

        } catch (IOException e) {
            log.error("Error parsing CSV file", e);
            throw new RuntimeException("CSV 파일 파싱 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * Stock이 있으면 조회하고, 없으면 생성
     */
    private Stock getOrCreateStock(String symbol, String stockName) {
        Optional<Stock> stockOptional = stockRepository.findById(symbol);

        if (stockOptional.isPresent()) {
            return stockOptional.get();
        } else {
            // Stock이 없으면 새로 생성
            Stock newStock = Stock.builder()
                    .stockId(symbol)
                    .stockName(stockName)
                    .build();

            Stock savedStock = stockRepository.save(newStock);
            log.info("Created new stock: symbol={}, stockName={}", symbol, stockName);
            return savedStock;
        }
    }

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
        // 사용자 존재 여부 확인
        Member member = memberService.findById(memberId);

        // 사용자의 보유종목 조회
        List<MemberStock> memberStocks = memberStockService.getMemberStocks(member);

        // 보유종목의 stockId 목록 추출
        List<String> stockIds = memberStocks.stream()
                .map(memberStock -> memberStock.getStock().getStockId())
                .collect(Collectors.toList());

        // 해당 주식들의 어닝콜 데이터 조회
        List<EarningCall> earningCalls = earningCallRepository.findByStockIds(stockIds);
        log.info("Found {} earning calls for member {} with {} stocks",
                earningCalls.size(), memberId, stockIds.size());

        return earningCalls;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EarningCall> getUpcomingEarningCall(Long memberId) {
        Member member = memberService.findById(memberId);
        List<MemberStock> memberStocks = memberStockService.getMemberStocks(member);

        List<String> stockIds = memberStocks.stream()
                .map(memberStock -> memberStock.getStock().getStockId())
                .toList();

        List<EarningCall> earningCalls = earningCallRepository.findByMemberId(memberId);
        return earningCalls.stream()
                .filter(earningCall ->
                        !LocalDate.parse(earningCall.getEarningCallDate()).isBefore(LocalDate.now(clock)))
                .sorted(Comparator.comparing((EarningCall ec) -> LocalDate.parse(ec.getEarningCallDate())))
                .toList();

    }
}
