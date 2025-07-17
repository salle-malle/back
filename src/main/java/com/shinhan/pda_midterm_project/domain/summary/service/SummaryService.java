package com.shinhan.pda_midterm_project.domain.summary.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import com.shinhan.pda_midterm_project.domain.stock.repository.StockRepository;
import com.shinhan.pda_midterm_project.domain.summary.model.Summary;
import com.shinhan.pda_midterm_project.domain.summary.repository.SummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SummaryService {

    @Value("${openai.api-key}")
    private String apiKey;

    private final SummaryRepository summaryRepository;

    private OpenAIClient client;

    private OpenAIClient getClient() {
        if (client == null) {
            client = OpenAIOkHttpClient.builder()
                    .apiKey(apiKey)
                    .build();
        }
        return client;
    }

    @Transactional
    public Summary summarizeAndSave(String content, Stock stock) {
        String summaryText = summarize(content);

        Summary summary = Summary.builder()
                .stock(stock)
                .newsContent(summaryText)
                .build();

        return summaryRepository.save(summary);
    }

    /**
     * 여러개의 미국 영어 기사들을 가지고 전체적인 요약
     * @param content
     * @return
     */
    private String summarize(String content) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_3_5_TURBO)
                .addUserMessage("너는 전문 투자 뉴스 요약가야. 내가 제공하는 영어 뉴스 기사들의 내용을 모두 읽고, 전체적으로 하나의 흐름으로 종합해 요약해줘.  \n"
                        + "- 한국어로 작성해.  \n"
                        + "- 투자자들이 한눈에 빠르게 이해할 수 있도록, 간결하고 자연스럽게 서술형으로 작성해.  \n"
                        + "- 구체적인 수치 예측이나 투자 조언, 기사 링크, 불필요한 서론이나 결론은 포함하지 마. :\n\n" + content)
                .maxCompletionTokens(500)
                .temperature(0.5)
                .build();

        ChatCompletion completion = getClient().chat()
                .completions()
                .create(params);

        return completion.choices().get(0).message().content().orElse("").trim();
    }

    /**
     * 성향에 따른 첨언 생성
     */
    public String generateCommentary(String summaryContent, String investmentTypeName) {
        String prompt = buildCommentaryPrompt(summaryContent, investmentTypeName);

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_3_5_TURBO)
                .addUserMessage(prompt)
                .maxCompletionTokens(1000)
                .temperature(0.7)
                .build();

        ChatCompletion completion = getClient().chat().completions().create(params);

        return completion.choices().get(0).message().content().orElse("").trim();
    }


    /**
     * 성향별 프롬프트 빌드
     */
    private String buildCommentaryPrompt(String summaryContent, String investmentTypeName) {
        String description = switch (investmentTypeName) {
            case "안정형" -> "안정형(원금 보전 최우선)";
            case "보수형" -> "보수형(소폭의 수익 추구, 낮은 위험)";
            case "적극형" -> "적극형(수익과 성장을 위해 일정 수준의 위험 감수)";
            case "공격형" -> "공격형(최대 수익 추구, 높은 위험 감수)";
            default -> "일반 투자자";
        };

        return "아래는 오늘의 투자 뉴스 핵심 요약 내용이야:\n\n" + summaryContent + "\n\n"
                + "사용자는 이 전체 내용을 모르는 상태야. 따라서 아래 조건을 만족하도록 짧고 간결하게 다시 전달해줘.\n"
                + "- 핵심 내용만 간단히 요약해서 한 문단으로 정리하고, 너무 자세히 나열하지 마.\n"
                + "- 이 사용자는 " + description + "를 지향하는 투자자야. 도움이 되는 관점을 포함해 작성해줘.\n"
                + "- 한국어로 작성해. 문장을 자연스럽게 마무리해. 서술형으로 존댓말로 투자 비서처럼 작성해줘. 500자 이내로 작성해줘";
    }
}
