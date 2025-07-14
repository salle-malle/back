package com.shinhan.pda_midterm_project.domain.notification.service;

import com.shinhan.pda_midterm_project.common.util.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SseEmitterRepository emitterRepository;

    /**
     * 클라이언트와 SSE 연결을 생성하고 emitter를 저장
     *
     * @param memberId 연결할 회원 ID
     * @return SseEmitter
     */
    public SseEmitter connect(Long memberId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitter.onCompletion(() -> emitterRepository.delete(memberId));
        emitter.onTimeout(() -> emitterRepository.delete(memberId));
        emitter.onError((e) -> emitterRepository.delete(memberId));

        emitterRepository.save(memberId, emitter);

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
}
