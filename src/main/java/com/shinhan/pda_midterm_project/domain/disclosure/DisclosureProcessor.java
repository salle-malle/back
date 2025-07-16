package com.shinhan.pda_midterm_project.domain.disclosure;

import com.shinhan.pda_midterm_project.common.util.FastApiClient;
import com.shinhan.pda_midterm_project.domain.disclosure.dto.DisclosureRequestDto;
import com.shinhan.pda_midterm_project.domain.disclosure.dto.DisclosureResponseDto;
import com.shinhan.pda_midterm_project.domain.disclosure.model.Disclosure;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DisclosureProcessor implements ItemProcessor<Stock, Disclosure> {

    private final FastApiClient fastApiClient;

    // 상태 저장 변수
    private List<Disclosure> currentDisclosures = List.of();
    private int index = 0;

    @Override
    public Disclosure process(Stock stock) {
        // 새로운 주식을 받으면 FastAPI 요청하고 리스트 초기화
        if (index == 0) {
            DisclosureRequestDto request = new DisclosureRequestDto(
                    stock.getStockId(), 1);

            DisclosureResponseDto response = fastApiClient.requestTodayDisclosure(request);

            // 예: summary는 결과 전체를 문자열로 합침
            String summary = response.results().stream()
                    .flatMap(r -> r.summary().stream())
                    .collect(Collectors.joining("\n"));

            currentDisclosures = response.results().stream()
                    .map(result -> Disclosure.create(
                            stock,
                            result.title(),
                            LocalDate.parse(result.filing_date()),
                            summary,
                            result.event_type()
                    ))
                    .toList();
        }

        if (index < currentDisclosures.size()) {
            return currentDisclosures.get(index++); // 하나씩 반환
        } else {
            index = 0;
            currentDisclosures = List.of();
            return null; // 현재 Stock 끝. 다음 Stock 넘어감
        }
    }
}
