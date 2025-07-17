package com.project.nyang.modules.notification.controller;

import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.modules.notification.service.NotificationService;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 알림기능 백엔드 테스트용 컨트롤러입니다
 *
 * @author : 이지은
 * @fileName : NotificationTestController
 * @since : 25. 7. 17.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications/test")
@Tag(name = "🔔 알림 기능 테스트", description = "알림 기능 수동 테스트용 API")
public class NotificationTestController {
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Operation(summary = "특정 사용자에게 알림 전송", description = "테스트용으로 알림을 강제로 생성하고 해당 사용자에게 WebSocket으로 전송합니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "알림 전송 완료"), @ApiResponse(responseCode = "404", description = "해당 사용자 없음")
    })
    @PostMapping("/{userId}")
    public ResponseEntity<String> sendTestNotification(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        notificationService.mailSentNotification(user);
        return ResponseEntity.ok("알림 전송 완료");
    }
}