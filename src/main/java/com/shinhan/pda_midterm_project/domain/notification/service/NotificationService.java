package com.shinhan.pda_midterm_project.domain.notification.service;

import com.shinhan.pda_midterm_project.common.util.SseEmitterRepository;
import com.shinhan.pda_midterm_project.domain.notification.model.Notification;
import com.shinhan.pda_midterm_project.domain.notification.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {

    private final SseEmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    public NotificationService(SseEmitterRepository emitterRepository, NotificationRepository notificationRepository) {
        this.emitterRepository = emitterRepository;
        this.notificationRepository = notificationRepository;
    }

    /**
     * 클라이언트와 SSE 연결을 생성하고 emitter를 저장
     *
     * @param memberId 연결할 회원 ID
     * @return SseEmitter
     */
    public SseEmitter connect(Long memberId) {
        log.info("✅ [connect] memberId: {} 접속 시도", memberId);
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitter.onCompletion(() -> emitterRepository.delete(memberId));
        emitter.onTimeout(() -> emitterRepository.delete(memberId));
        emitter.onError((e) -> emitterRepository.delete(memberId));

        emitterRepository.save(memberId, emitter);
        log.info("✅ [connect] memberId: {} 접속 성공, 현재 emitterMap: {}", memberId, emitterRepository.getEmitterMap());


        return emitter;
    }

    /**
     * 특정 회원에게 알림 전송
     *
     * @param memberId 회원 ID
     * @param data     전송할 데이터
     */
    public void sendNotification(Long memberId, String data) {
        log.info(">>> [알림 전송 시도] memberId: {}", memberId);
        SseEmitter emitter = emitterRepository.get(memberId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .data(data));
                log.info(">>> [알림 전송 완료]");
            } catch (Exception e) {
                // 전송 실패 시 emitterMap에서 제거
                emitterRepository.delete(memberId);
                log.warn(">>> [알림 전송 실패] emitter 삭제", e);
            }
        } else {
            log.warn(">>> [알림 전송 실패] emitter 없음");
        }
    }

    public Set<Long> getConnectedUserIds() {
        return emitterRepository.getEmitterMap().keySet();
    }

    public List<Notification> getTodaysNotifications(Long memberId) {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        return notificationRepository.findByMemberIdAndCreatedAtBetween(memberId, startOfDay, endOfDay);
    }

}
