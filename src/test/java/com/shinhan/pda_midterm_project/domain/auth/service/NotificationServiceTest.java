package com.shinhan.pda_midterm_project.domain.auth.service;

import com.shinhan.pda_midterm_project.common.util.SseEmitterRepository;
import com.shinhan.pda_midterm_project.domain.notification.repository.NotificationRepository;
import com.shinhan.pda_midterm_project.domain.notification.service.NotificationService;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class NotificationServiceTest {

    private NotificationService notificationService;
    private SseEmitterRepository emitterRepository;
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        emitterRepository = new SseEmitterRepository();
        notificationRepository = mock(NotificationRepository.class);
        notificationService = new NotificationService(emitterRepository, notificationRepository);
    }

    @Test
    void 여러_유저_접속_모사_및_ID_출력_테스트() {
        // Given — 유저 3명 접속 시뮬

        notificationService.connect(4L);
        notificationService.connect(5L);
        notificationService.connect(6L);
        notificationService.connect(7L);
        notificationService.connect(8L);
        notificationService.connect(9L);
        notificationService.connect(10L);
        notificationService.connect(11L);
        notificationService.connect(12L);
        notificationService.connect(13L);

        System.out.println(">>> emitterMap 상태: " + emitterRepository.getEmitterMap());

        // When — 현재 접속 중인 유저 ID 가져오기
        Set<Long> connectedIds = notificationService.getConnectedUserIds();

        // Then — ID 출력
        connectedIds.forEach(id -> System.out.println("접속 중인 유저 ID: " + id));

        // 검증
        assertThat(connectedIds).containsExactlyInAnyOrder(4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L);
    }

}
