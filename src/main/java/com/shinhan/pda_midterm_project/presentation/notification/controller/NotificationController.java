package com.shinhan.pda_midterm_project.presentation.notification.controller;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.annotation.MemberOnly;
import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.notification.service.NotificationService;
import com.shinhan.pda_midterm_project.common.response.Response;

import com.shinhan.pda_midterm_project.presentation.notification.dto.NotificationResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
     * @return SseEmitter (서버 → 클라이언트 실시간 통로)
     */
    @GetMapping("/stream")
    @MemberOnly
    public SseEmitter connect(@Auth Accessor accessor) {
        Long memberId = accessor.memberId();
        return notificationService.connect(memberId);
    }

    @GetMapping
    @MemberOnly
    public ResponseEntity<Response<Object>> getMemberNotifications(@Auth Accessor accessor) {
        Long memberId = accessor.memberId();
        List<NotificationResponseDto> result = notificationService.getByMemberId(memberId);
        return ResponseEntity.ok(Response.success("200", "알림 조회 성공", result));
    }

    @PatchMapping("/{notificationId}/read")
    @MemberOnly
    public ResponseEntity<Response<Object>> markAsRead(@Auth Accessor accessor,
                                                       @PathVariable Long notificationId) {
        Long memberId = accessor.memberId();
        notificationService.markAsRead(notificationId, memberId);
        return ResponseEntity.ok(Response.success(
                ResponseMessages.MARK_NOTIFICATION_AS_READ_SUCCESS.getCode(),
                ResponseMessages.MARK_NOTIFICATION_AS_READ_SUCCESS.getMessage()
        ));
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
