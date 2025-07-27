package com.shinhan.pda_midterm_project.domain.total_summary.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.repository.MemberRepository;
import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.model.MemberStockSnapshot;
import com.shinhan.pda_midterm_project.domain.member_stock_snapshot.repository.MemberStockSnapshotRepository;
import com.shinhan.pda_midterm_project.domain.notification.model.Notification;
import com.shinhan.pda_midterm_project.domain.notification.model.NotificationType;
import com.shinhan.pda_midterm_project.domain.notification.repository.NotificationRepository;
import com.shinhan.pda_midterm_project.domain.total_summary.model.TotalSummary;
import com.shinhan.pda_midterm_project.domain.total_summary.repository.TotalSummaryRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;

@Slf4j
@Service
@RequiredArgsConstructor
public class TotalSummaryService {

    @Value("${openai.api-key}")
    private String apiKey;

    private final MemberRepository memberRepository;
    private final MemberStockSnapshotRepository snapshotRepository;
    private final TotalSummaryRepository totalSummaryRepository;
    private final NotificationRepository notificationRepository;
    private final Clock clock;

    private OpenAIClient client;

    private OpenAIClient getClient() {
        if (client == null) {
            client = OpenAIOkHttpClient.builder()
                    .apiKey(apiKey)
                    .build();
        }
        return client;
    }

    @Transactional(readOnly = true)
    public void generateTotalSummaryForAllMembers() {
        List<Member> members = memberRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Member member : members) {
            Long memberId = member.getId();
            List<MemberStockSnapshot> snapshots = snapshotRepository.findAllByMemberId(memberId);

            List<MemberStockSnapshot> todaySnapshots = snapshots.stream()
                    .filter(snapshot -> snapshot.getCreatedAt().toLocalDate().isEqual(today))
                    .toList();

            if (todaySnapshots.isEmpty()) continue;

            String merged = todaySnapshots.stream()
                    .map(s -> s.getInvestmentTypeNewsComment().getInvestmentTypeNewsContent())
                    .collect(Collectors.joining("\n"));

            String summary = summarizeTotal(merged);
            log.info("🧾 Member ID: {}, Total Summary:\n{}", memberId, summary);

            TotalSummary entity = TotalSummary.builder()
                    .member(member)
                    .totalContent(summary)
                    .build();

            totalSummaryRepository.save(entity);

            String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"));
            int stockCount = todaySnapshots.size();
            String title = String.format("%s 총평 요약 도착!", formattedDate);
            String content = String.format("%d개 종목에 대한 총평 요약이 도착했어요. 확인해보세요!", stockCount);


            Notification notification = Notification.builder()
                    .member(member)
                    .notificationTitle(title)
                    .notificationContent(content)
//                    .notificationUrl("/total-summary")
                    .notificationIsRead(false)
                    .notificationType(NotificationType.SUMMARY_COMPLETE)
                    .build();

            notificationRepository.save(notification);
        }
    }

    private String summarizeTotal(String content) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_3_5_TURBO)
                .addUserMessage("""
                    다음은 오늘의 투자 뉴스 요약 모음입니다. 이 내용을 바탕으로 3~5개의 핵심 요약을 만들어 주세요.
                    
                    각 요약은 다음과 같은 형식으로 구성해 주세요:
                    
                    [이모지] 제목
                    한 줄설명 
                    
                    예시:
                    📉 기술주 약세
                    금리 인상 우려로 기술주 중심의 하락세가 나타났습니다.
                                        
                    📈 반도체 강세
                    AI 수요 확대에 따라 엔비디아 등 반도체 종목이 상승했습니다.
                                        
                    💡 투자 코멘트
                    단기적인 시장 변동성에 대비해 포트폴리오 리밸런싱이 필요합니다.
                    
                    상승, 하락, 전반적인 투자 코멘트, 투자 조언 으로 구성해주세요. 이모지는 적절히 📉📈💡🔥 같은 걸 활용해 주셔도 좋습니다.
                    각 문단별로 \n\n로 구분해주세요.
                    """.trim() + "\n\n" + content)
                .maxCompletionTokens(500)
                .temperature(0.5)
                .build();

        ChatCompletion completion = getClient().chat().completions().create(params);

        return completion.choices().get(0).message().content().orElse("").trim();
    }

    public String getTodaySummary(Long memberId) {
        LocalDate today = LocalDate.now(clock);
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay().minusNanos(1);

        return totalSummaryRepository.getTodayTotalSummary(memberId, startOfDay, endOfDay);
    }
}
