package com.shinhan.pda_midterm_project.common.config;

import com.shinhan.pda_midterm_project.common.util.FastApiClient;
import com.shinhan.pda_midterm_project.common.util.TimeUtil;
import com.shinhan.pda_midterm_project.domain.disclosure.dto.DisclosureRequestDto;
import com.shinhan.pda_midterm_project.domain.disclosure.dto.DisclosureResponseDto;
import com.shinhan.pda_midterm_project.domain.disclosure.model.Disclosure;
import com.shinhan.pda_midterm_project.domain.disclosure.repository.DisclosureRepository;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import com.shinhan.pda_midterm_project.domain.stock.repository.StockRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class DisclosureInitBatch {
    private final StockRepository stockRepository;
    private final DisclosureRepository disclosureRepository;
    private final Clock clock;
    private final FastApiClient fastApiClient;

    public void runDisclosureBatch() {
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            String stockId = stock.getStockId();
            System.out.println(stockId + " 분석을 시작합니다");

            DisclosureRequestDto request = DisclosureRequestDto.create(
                    stockId, LocalDate.now(clock).minusDays(365).toString(),
                    LocalDate.now(clock).toString());

            DisclosureResponseDto response = fastApiClient.requestDisclosure(request);

            if (response.results().isEmpty()) {
                continue;
            }
            List<Disclosure> disclosures = response.results().stream()
                    .map(disclosureResult -> {
                        LocalDate filingDate = TimeUtil.stringToLocalDate(disclosureResult.filing_date());
                        return Disclosure.create(
                                stock,
                                disclosureResult.title(),
                                filingDate,
                                disclosureResult.narrative()
                        );
                    }).toList();
            disclosureRepository.saveAll(disclosures);
        }
    }
}
