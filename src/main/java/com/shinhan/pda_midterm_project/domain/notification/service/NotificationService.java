package com.shinhan.pda_midterm_project.domain.notification.service;

import com.shinhan.pda_midterm_project.common.util.SseEmitterRepository;
import com.shinhan.pda_midterm_project.domain.member.model.Member;
import com.shinhan.pda_midterm_project.domain.member.repository.MemberRepository;
import com.shinhan.pda_midterm_project.domain.notification.model.Notification;
import com.shinhan.pda_midterm_project.domain.notification.repository.NotificationRepository;
import com.shinhan.pda_midterm_project.presentation.notification.dto.NotificationResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {

    private final SseEmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public NotificationService(SseEmitterRepository emitterRepository, NotificationRepository notificationRepository, MemberRepository memberRepository) {
        this.emitterRepository = emitterRepository;
        this.notificationRepository = notificationRepository;
        this.memberRepository = memberRepository;
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

    public List<NotificationResponseDto> getByMemberId(Long memberId) {
        return notificationRepository.findByMemberId(memberId).stream()
                .map(n -> NotificationResponseDto.builder()
                        .id(n.getId())
                        .title(n.getNotificationTitle())
                        .message(n.getNotificationContent())
                        .read(n.getNotificationIsRead())
                        .time(n.getCreatedAt())
                        .type(n.getNotificationType().name())
                        .build())
                .toList();
    }

    @Transactional
    public void markAsRead(Long notificationId, Long memberId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 알림이 존재하지 않습니다."));

        if (!notification.getMember().getId().equals(memberId)) {
            throw new SecurityException("알림 소유자가 아닙니다.");
        }

        if (!Boolean.TRUE.equals(notification.getNotificationIsRead())) {
            notification.markAsRead(); // 엔티티에서 처리
            notificationRepository.save(notification);
        }
    }

    public boolean hasUnreadNotifications(Long memberId) {
        return notificationRepository.existsByMemberIdAndNotificationIsReadFalse(memberId);
    }

}
