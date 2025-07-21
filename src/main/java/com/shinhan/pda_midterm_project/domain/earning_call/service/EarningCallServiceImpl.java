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
import org.springframework.web.multipart.MultipartFile;

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
  public void parseAndSaveEarningCalls(MultipartFile csvFile) {
    try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))) {

      String line;
      boolean isFirstLine = true;
      List<EarningCall> earningCalls = new ArrayList<>();
      int processedCount = 0;
      int savedCount = 0;

      while ((line = reader.readLine()) != null) {
        if (isFirstLine) {
          isFirstLine = false;
          continue; // 헤더 스킵
        }

        String[] columns = line.split(",");
        if (columns.length >= 3) {
          String symbol = columns[0].trim();
          String stockName = columns[1].trim();
          String reportDate = columns[2].trim();
          processedCount++;

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

      if (!earningCalls.isEmpty()) {
        earningCallRepository.saveAll(earningCalls);
        log.info("Processed {} records, saved {} earning call records", processedCount, savedCount);
      } else {
        log.warn("No earning calls to save. Processed {} records but no valid data found.", processedCount);
      }

    } catch (IOException e) {
      log.error("Error parsing CSV file", e);
      throw new RuntimeException("CSV 파일 파싱 중 오류가 발생했습니다.", e);
    }
  }

  private Stock getOrCreateStock(String symbol, String stockName) {
    Optional<Stock> stockOptional = stockRepository.findById(symbol);

    if (stockOptional.isPresent()) {
      Stock existingStock = stockOptional.get();
      if (!stockName.equals(existingStock.getStockName())) {
        Stock updatedStock = Stock.builder()
                .stockId(existingStock.getStockId())
                .stockName(stockName)
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
                .ovrsItemName(existingStock.getOvrsItemName())
                .ovrsExcgCd(existingStock.getOvrsExcgCd())
                .trCrcyCd(existingStock.getTrCrcyCd())
                .prdtTypeCd(existingStock.getPrdtTypeCd())
                .build();

        return stockRepository.save(updatedStock);
      }
      return existingStock;
    } else {
      Stock newStock = Stock.builder()
              .stockId(symbol)
              .stockName(stockName)
              .build();
      return stockRepository.save(newStock);
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
    Member member = memberService.findById(memberId);
    List<MemberStock> memberStocks = memberStockService.getMemberStocks(member);
    List<String> stockIds = memberStocks.stream()
            .map(ms -> ms.getStock().getStockId())
            .collect(Collectors.toList());
    return earningCallRepository.findByStockIds(stockIds);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EarningCall> getUpcomingEarningCall(Long memberId) {
    Member member = memberService.findById(memberId);
    List<MemberStock> memberStocks = memberStockService.getMemberStocks(member);
    List<String> stockIds = memberStocks.stream()
            .map(ms -> ms.getStock().getStockId())
            .toList();

    List<EarningCall> earningCalls = earningCallRepository.findByMemberId(memberId);
    return earningCalls.stream()
            .filter(ec -> !LocalDate.parse(ec.getEarningCallDate()).isBefore(LocalDate.now(clock)))
            .sorted(Comparator.comparing(ec -> LocalDate.parse(ec.getEarningCallDate())))
            .toList();
  }
}
