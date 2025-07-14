package com.shinhan.pda_midterm_project.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SseEmitter 객체를 관리하는 저장소.
 * DB repository가 아닌 메모리 기반의 emitter 저장/관리용 유틸 클래스
 */
@Component
public class SseEmitterRepository {
    private final Map<Long, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    /**
     * emitter 저장
     */
    public SseEmitter save(Long memberId, SseEmitter emitter) {
        emitterMap.put(memberId, emitter);
        return emitter;
    }

    /**
     * emitter 조회
     */
    public SseEmitter get(Long memberId) {
        return emitterMap.get(memberId);
    }

    /**
     * emitter 삭제
     */
    public void delete(Long memberId) {
        emitterMap.remove(memberId);
    }
}
