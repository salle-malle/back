package com.shinhan.pda_midterm_project.domain.auth.service;

import com.shinhan.pda_midterm_project.common.util.SseEmitterRepository;
import com.shinhan.pda_midterm_project.domain.notification.service.NotificationService;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    private NotificationService notificationService;
    private SseEmitterRepository emitterRepository;

    @BeforeEach
    void setUp() {
        emitterRepository = new SseEmitterRepository();
        notificationService = new NotificationService(emitterRepository);
    }

    @Test
    void 여러_유저_접속_모사_및_ID_출력_테스트() {
        // Given — 유저 3명 접속 시뮬
        notificationService.connect(1L);
        notificationService.connect(2L);
        notificationService.connect(3L);

        System.out.println(">>> emitterMap 상태: " + emitterRepository.getEmitterMap());

        // When — 현재 접속 중인 유저 ID 가져오기
        Set<Long> connectedIds = notificationService.getConnectedUserIds();

        // Then — ID 출력
        connectedIds.forEach(id -> System.out.println("접속 중인 유저 ID: " + id));

        // 검증
        assertThat(connectedIds).containsExactlyInAnyOrder(1L, 2L, 3L);
    }

}
