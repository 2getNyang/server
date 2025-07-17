package com.project.nyang.modules.notification.controller;

import com.project.nyang.global.security.core.CustomUserDetails;
import com.project.nyang.modules.notification.dto.NotificationDTO;
import com.project.nyang.modules.notification.entity.Notification;
import com.project.nyang.modules.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 알림 기능 컨트롤러 입니다
 *
 * @author : 이지은
 * @fileName : NotificationController
 * @since : 25. 7. 17.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications/")
@Tag(name="🔔 채팅 알림 기능", description = "예상으로 임시 구현해서 수정 필요, 사용 X")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "모든 알림 조회",description = "테스트 X")
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Notification> notifications = notificationService.getNotifications(userDetails.getUser());
        return ResponseEntity.ok(notifications.stream().map(NotificationDTO::new).toList());
    }

    @Operation(summary = "미확인 알림 읽음 수정",description = "테스트 X")
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.markAsRead(id, userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "안읽은 알림 조회", description = "테스트 X")
    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadCount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(notificationService.countUnreadNotifications(userDetails.getUser()));
    }

}