package com.shinhan.pda_midterm_project.domain.summary.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SummaryService {

    @Value("${openai.api-key}")
    private String apiKey;

    private OpenAIClient client;

    private OpenAIClient getClient() {
        if (client == null) {
            client = OpenAIOkHttpClient.builder()
                    .apiKey(apiKey)
                    .build();
        }
        return client;
    }

    public String summarize(String content) {
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

}
