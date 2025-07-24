package com.shinhan.pda_midterm_project.domain.earning_call.service;

import com.shinhan.pda_midterm_project.domain.earning_call.model.EarningCall;
import com.shinhan.pda_midterm_project.domain.earning_call.repository.EarningCallRepository;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member_stock.model.MemberStock;
import com.shinhan.pda_midterm_project.domain.member_stock.repository.MemberStockRepository;
import com.shinhan.pda_midterm_project.domain.notification.model.Notification;
import com.shinhan.pda_midterm_project.domain.notification.repository.NotificationRepository;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
import com.shinhan.pda_midterm_project.domain.notification.model.NotificationType;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EarningCallAlarmScheduler {

    private final EarningCallRepository earningCallRepository;
    private final MemberStockRepository memberStockRepository;
    private final NotificationRepository notificationRepository;

    @Scheduled(cron = "0 30 7 * * * ")
    @Transactional
    public void sendEarningCallNotification() {
        String today = LocalDate.now().toString();
        List<EarningCall> todayCalls = earningCallRepository.findByEarningCallDate(today);

        for (EarningCall call : todayCalls) {
            Stock stock = call.getStock();
            List<MemberStock> holders = memberStockRepository.findByStock(stock);

            for (MemberStock ms : holders) {
                Member member = ms.getMember();

                Notification notification = Notification.builder()
                        .member(member)
                        .notificationTitle("오늘의 어닝콜 알림")
                        .notificationContent(stock.getOvrsItemName() + " 종목이 오늘 어닝콜 예정입니다.")
                        .notificationIsRead(false)
                        .notificationUrl(null)
                        .notificationType(NotificationType.EARNINGS_CALL)
                        .build();

                notificationRepository.save(notification);
                log.info(">>> [EarningCall] 오늘의 어닝콜 저장 완료");
            }
        }
    }
}
