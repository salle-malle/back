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
                .addUserMessage("아래 기사를 3~5 문장으로 요약해줘:\n\n" + content)
                .maxCompletionTokens(500)
                .temperature(0.7)
                .build();

        ChatCompletion completion = getClient().chat()
                .completions()
                .create(params);

        return completion.choices().get(0).message().content().orElse("").trim();
    }

}
