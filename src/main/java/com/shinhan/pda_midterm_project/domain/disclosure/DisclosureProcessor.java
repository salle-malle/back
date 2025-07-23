package com.shinhan.pda_midterm_project.domain.disclosure;

import com.shinhan.pda_midterm_project.common.util.FastApiClient;
import com.shinhan.pda_midterm_project.common.util.TimeUtil;
import com.shinhan.pda_midterm_project.domain.disclosure.dto.DisclosureRequestDto;
import com.shinhan.pda_midterm_project.domain.disclosure.dto.DisclosureResponseDto;
import com.shinhan.pda_midterm_project.domain.disclosure.model.Disclosure;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DisclosureProcessor implements ItemProcessor<Stock, Disclosure> {

    private final FastApiClient fastApiClient;
    private List<Disclosure> currentDisclosures = List.of();
    private int index = 0;
    private final Clock clock;

    @Override
    public Disclosure process(Stock stock) {
        if (index == 0) {
            DisclosureRequestDto request = DisclosureRequestDto.create(
                    stock.getStockId(), LocalDate.now(clock).minusDays(1).toString(), LocalDate.now(clock).toString());

            DisclosureResponseDto response = fastApiClient.requestDisclosure(request);
            System.out.println(response.results());
            if (response.results().isEmpty()) {
                index = 0;
                currentDisclosures = List.of();
                return null;
            }

            currentDisclosures = response.results().stream()
                    .map(result -> {
                        try {
                            if (result.filing_date() == null || result.filing_date().trim().isEmpty()) {
                                return null;
                            }

                            LocalDate filingDate = TimeUtil.stringToLocalDate(result.filing_date());
                            return Disclosure.create(
                                    stock,
                                    result.title(),
                                    filingDate,
                                    result.narrative()
                            );
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();
        }

        if (index < currentDisclosures.size()) {
            return currentDisclosures.get(index++);
        } else {
            index = 0;
            currentDisclosures = List.of();
            return null;
        }
    }
}