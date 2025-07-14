package com.shinhan.pda_midterm_project.presentation.notification.controller;

import com.shinhan.pda_midterm_project.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@CrossOrigin(originPatterns = "*", allowCredentials = "true")
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 클라이언트가 SSE 연결을 요청할 때 호출되는 엔드포인트
     * 로그인 구현 후 memberId 추출 방식만 바꾸면 될듯
     *
     * @param memberId 클라이언트의 회원 ID
     * @return SseEmitter (서버 → 클라이언트 실시간 통로)
     */
    @GetMapping("/stream")
    public SseEmitter connect(@RequestParam Long memberId) {
        return notificationService.connect(memberId);
    }

    /**
     * 테스트용 알림 전송 API
     * (Postman으로 호출하여 프론트에 알림이 도착하는지 테스트 가능)
     *
     * @param memberId 알림을 보낼 회원 ID
     */
    @PostMapping("/test-send")
    public void sendTestNotification(@RequestParam Long memberId) {
        String message = "테스트 알림";
        notificationService.sendNotification(memberId, message);
    }
}
