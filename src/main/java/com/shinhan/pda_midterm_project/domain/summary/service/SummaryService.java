package com.shinhan.pda_midterm_project.domain.summary.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.shinhan.pda_midterm_project.domain.investment_type.repository.InvestmentTypeRepository;
import com.shinhan.pda_midterm_project.domain.investment_type_news_comment.model.InvestmentTypeNewsComment;
import com.shinhan.pda_midterm_project.domain.investment_type_news_comment.repository.InvestmentTypeNewsCommentRepository;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.repository.MemberRepository;
import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;
import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.repository.MemberStockSnapshotRepository;
import com.shinhan.pda_midterm_project.domain.news.model.News;
import com.shinhan.pda_midterm_project.domain.news.repository.NewsRepository;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import com.shinhan.pda_midterm_project.domain.summary.model.Summary;
import com.shinhan.pda_midterm_project.domain.summary.repository.SummaryRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryService {

    @Value("${openai.api-key}")
    private String apiKey;

    private final SummaryRepository summaryRepository;
    private final InvestmentTypeRepository investmentTypeRepository;
    private final InvestmentTypeNewsCommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final MemberStockSnapshotRepository memberStockSnapshotRepository;
    private final NewsRepository newsRepository;


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
    protected Summary summarizeAndSave(String content, Stock stock, String imageUrl) {
        String summaryText = summarize(content);

        // 1. Summary 저장
        Summary summary = summaryRepository.save(
                Summary.builder()
                        .stock(stock)
                        .newsContent(summaryText)
                        .newsImage(imageUrl)
                        .build()
        );

        // 2. 투자 성향별 첨언 저장
        List<InvestmentTypeNewsComment> savedComments = investmentTypeRepository.findAll().stream()
                .map(type -> {
                    String comment = generateCommentary(summaryText, type.getInvestmentName());
                    InvestmentTypeNewsComment commentEntity = InvestmentTypeNewsComment.builder()
                            .summary(summary)
                            .investmentType(type)
                            .investmentTypeNewsContent(comment)
                            .build();
                    return commentRepository.save(commentEntity);
                })
                .toList();

        // 3. 종목을 보유한 멤버 조회
        List<Member> holdingMembers = memberRepository.findAllByStockId(stock.getStockId());

        for (Member member : holdingMembers) {
            Long memberTypeId = member.getInvestmentType().getId();
            log.info("memberId: {}, 투자성향 ID: {}", member.getId(), memberTypeId);
//            InvestmentType memberType = member.getInvestmentType();
            if (memberTypeId == null) {
                continue;
            }
            InvestmentTypeNewsComment matchedComment = savedComments.stream()
                    .filter(c -> c.getInvestmentType().getId().equals(memberTypeId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("투자 성향에 맞는 첨언이 없습니다."));

            // 5. MemberStockSnapshot 저장
            MemberStockSnapshot snapshot = MemberStockSnapshot.builder()
                    .member(member)
                    .investmentTypeNewsComment(matchedComment)
                    .build();

            memberStockSnapshotRepository.save(snapshot);
        }

        return summary;
    }


    /**
     * 여러개의 미국 영어 기사들을 가지고 전체적인 요약
     * @param content
     * @return
     */
    private String summarize(String content) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_3_5_TURBO)
                .addUserMessage(
                        "⚠️ 너는 이제부터 전문 투자 뉴스 요약가야. 아래 영어 기사들을 읽고 **반드시 '한국어'로** 요약해야 해.\n\n"
                                + "요약은 **마크다운 형식**으로 다음 기준을 따라:\n"
                                + "1. 소제목은 `###`로 시작하고, 핵심 주제를 중심으로 작성해.\n"
                                + "2. 각 소제목 아래에 1~3줄로 문단을 구성하고, 불필요한 결론이나 서론은 생략해.\n"
                                + "3. 과도한 숫자 예측, 투자 조언, 기사 링크는 포함하지 마.\n"
                                + "4. 전체 소제목은 2~3개 정도만 사용해.\n\n"
                                + "⛔ *요약 결과가 영어일 경우, 응답은 무효 처리되며 평가에 반영되지 않아.*\n\n"
                                + "아래는 영어 기사 내용이야. **무조건 한국어로 요약해줘**:\n\n"
                                + content
                )
//                .maxCompletionTokens(500)
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
    private String generateCommentary(String summaryContent, String investmentTypeName) {
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

        return " 사용자는 아래 뉴스 요약을 이미 읽었어. 이 요약을 바탕으로, " + description + " 투자 성향을 가진 사용자에게 도움이 될 만한 짧은 코멘트를 해줘." +
                "- 너무 자세한 설명보다는, 요약 내용을 투자자 입장에서 어떻게 받아들이면 좋을지 한 문장 정도의 조언이나 인사이트를 줘." +
                "- 존댓말로 작성해줘. 투자 도우미처럼 말해줘." +
                "- 최대 200자 이내로 간결하게 작성해줘." +
                "- 형식은 자연스러운 서술형 문장 한 문장으로 해줘." +
                "뉴스 요약:" + summaryContent;
    }


    public void generateSummaryForTodayNews() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        List<News> todayNews = newsRepository.findAllByCreatedAtBetween(startOfDay, endOfDay);

        // 종목별로 뉴스 그룹핑
        Map<Stock, List<News>> newsByStock = todayNews.stream()
                .filter(n -> n.getStock() != null)
                .collect(Collectors.groupingBy(News::getStock));

        for (Map.Entry<Stock, List<News>> entry : newsByStock.entrySet()) {
            Stock stock = entry.getKey();
            List<News> newsList = entry.getValue();

            // 뉴스 내용 합치기
            String combinedContent = newsList.stream()
                    .map(News::getNewsContent)
                    .collect(Collectors.joining("\n\n"));

            String representativeImageUrl = newsList.stream()
                    .map(News::getNewsImage)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);

            // 요약 및 저장
            summarizeAndSave(combinedContent, stock, representativeImageUrl);
        }
    }


}
