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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .addUserMessage("다음은 오늘 투자 요약 내용 모음이야. 핵심을 한 문단으로 종합해서 한국어로 서술형 요약해줘.\n\n" + content)
                .maxCompletionTokens(500)
                .temperature(0.5)
                .build();

        ChatCompletion completion = getClient().chat().completions().create(params);

        return completion.choices().get(0).message().content().orElse("").trim();
    }
}
