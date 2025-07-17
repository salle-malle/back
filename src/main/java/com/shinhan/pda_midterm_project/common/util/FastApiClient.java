package com.shinhan.pda_midterm_project.common.util;

import com.shinhan.pda_midterm_project.domain.disclosure.dto.DisclosureRequestDto;
import com.shinhan.pda_midterm_project.domain.disclosure.dto.DisclosureResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class FastApiClient {

    private final WebClient fastApiWebClient;

    public DisclosureResponseDto requestTodayDisclosure(DisclosureRequestDto requestDto) {
        return fastApiWebClient.post()
                .uri("/analyze-8k")
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(DisclosureResponseDto.class)
                .block();
    }
}
