package com.shinhan.pda_midterm_project.domain.earning_call.service;

import com.shinhan.pda_midterm_project.domain.earning_call.model.EarningCall;
import com.shinhan.pda_midterm_project.domain.earning_call.repository.EarningCallRepository;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.repository.MemberRepository;
import com.shinhan.pda_midterm_project.domain.member_stock.model.MemberStock;
import com.shinhan.pda_midterm_project.domain.member_stock.repository.MemberStockRepository;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import com.shinhan.pda_midterm_project.domain.stock.repository.StockRepository;
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
  private final MemberRepository memberRepository;
  private final MemberStockRepository memberStockRepository;

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
   * Stock이 있으면 stockName을 업데이트하고, 없으면 생성
   */
  private Stock getOrCreateStock(String symbol, String stockName) {
    Optional<Stock> stockOptional = stockRepository.findById(symbol);

    if (stockOptional.isPresent()) {
      Stock existingStock = stockOptional.get();
      // 기존 Stock의 stockName을 CSV의 영문명으로 업데이트
      if (!stockName.equals(existingStock.getStockName())) {
        Stock updatedStock = Stock.builder()
            .stockId(existingStock.getStockId())
            .stockName(stockName) // CSV의 영문명으로 업데이트
            .rsym(existingStock.getRsym())
            .zdiv(existingStock.getZdiv())
            .curr(existingStock.getCurr())
            .vnit(existingStock.getVnit())
            .open(existingStock.getOpen())
            .high(existingStock.getHigh())
            .low(existingStock.getLow())
            .last(existingStock.getLast())
            .base(existingStock.getBase())
            .pvol(existingStock.getPvol())
            .pamt(existingStock.getPamt())
            .tvol(existingStock.getTvol())
            .tamt(existingStock.getTamt())
            .uplp(existingStock.getUplp())
            .dnlp(existingStock.getDnlp())
            .h52p(existingStock.getH52p())
            .h52d(existingStock.getH52d())
            .l52p(existingStock.getL52p())
            .l52d(existingStock.getL52d())
            .perx(existingStock.getPerx())
            .pbrx(existingStock.getPbrx())
            .epsx(existingStock.getEpsx())
            .bpsx(existingStock.getBpsx())
            .shar(existingStock.getShar())
            .mcap(existingStock.getMcap())
            .tomv(existingStock.getTomv())
            .tXprc(existingStock.getTXprc())
            .tXdif(existingStock.getTXdif())
            .tXrat(existingStock.getTXrat())
            .tRate(existingStock.getTRate())
            .tXsgn(existingStock.getTXsgn())
            .pXprc(existingStock.getPXprc())
            .pXdif(existingStock.getPXdif())
            .pXrat(existingStock.getPXrat())
            .pRate(existingStock.getPRate())
            .pXsng(existingStock.getPXsng())
            .eOrdyn(existingStock.getEOrdyn())
            .eHogau(existingStock.getEHogau())
            .eIcod(existingStock.getEIcod())
            .eParp(existingStock.getEParp())
            .etypNm(existingStock.getEtypNm())
            .stockImageUri(existingStock.getStockImageUri())
            .stockIsDelisted(existingStock.getStockIsDelisted())
            .ovrsPdno(existingStock.getOvrsPdno())
            .ovrsItemName(existingStock.getOvrsItemName()) // 한글명은 유지
            .ovrsExcgCd(existingStock.getOvrsExcgCd())
            .trCrcyCd(existingStock.getTrCrcyCd())
            .prdtTypeCd(existingStock.getPrdtTypeCd())
            .build();

        Stock savedStock = stockRepository.save(updatedStock);
        log.info("Updated stock: symbol={}, stockName={} (was: {})", symbol, stockName, existingStock.getStockName());
        return savedStock;
      }
      return existingStock;
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
    Optional<Member> memberOptional = memberRepository.findById(memberId);
    if (memberOptional.isEmpty()) {
      log.warn("Member not found with id: {}", memberId);
      return new ArrayList<>();
    }

    // 사용자의 보유종목 조회
    List<MemberStock> memberStocks = memberStockRepository.findByMember(memberOptional.get());
    if (memberStocks.isEmpty()) {
      log.info("No stocks found for member id: {}", memberId);
      return new ArrayList<>();
    }

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
}