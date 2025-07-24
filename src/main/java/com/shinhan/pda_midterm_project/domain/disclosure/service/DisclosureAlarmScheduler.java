package com.shinhan.pda_midterm_project.domain.disclosure.service;

import com.shinhan.pda_midterm_project.domain.disclosure.model.Disclosure;
import com.shinhan.pda_midterm_project.domain.disclosure.repository.DisclosureRepository;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member_stock.model.MemberStock;
import com.shinhan.pda_midterm_project.domain.member_stock.repository.MemberStockRepository;
import com.shinhan.pda_midterm_project.domain.notification.model.Notification;
import com.shinhan.pda_midterm_project.domain.notification.model.NotificationType;
import com.shinhan.pda_midterm_project.domain.notification.repository.NotificationRepository;
import com.shinhan.pda_midterm_project.domain.stock.model.Stock;
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
public class DisclosureAlarmScheduler {

    private final DisclosureRepository disclosureRepository;
    private final MemberStockRepository memberStockRepository;
    private final NotificationRepository notificationRepository;

//    @Scheduled(cron = "0 40 7 * * *")
    @Transactional
    @Scheduled(cron = "0 */1 * * * ?")
    public void sendDisclosureNotifications() {
        LocalDate today = LocalDate.now();
        List<Disclosure> todayDisclosures = disclosureRepository.findByDisclosureDate(today);

        for (Disclosure disclosure : todayDisclosures) {
            Stock stock = disclosure.getStock();
            List<MemberStock> holders = memberStockRepository.findByStock(stock);

            for (MemberStock memberStock : holders) {
                Member member = memberStock.getMember();

                Notification notification = Notification.builder()
                        .member(member)
                        .notificationTitle("오늘의 공시 - " + stock.getOvrsItemName())
                        .notificationContent(disclosure.getDisclosureTitle())
                        .notificationIsRead(false)
                        .notificationType(NotificationType.DISCLOSURE)
                        .notificationUrl("/stocks/" + stock.getRsym())
                        .build();

                notificationRepository.save(notification);
            }
        }
        log.info(">>> [Disclosure] 공시 전송 완료");
    }
}
