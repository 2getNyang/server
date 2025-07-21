package com.project.nyang.modules.notification.controller;

import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.global.security.core.CustomUserDetails;
import com.project.nyang.modules.chat.entity.ChatRoom;
import com.project.nyang.modules.chat.repository.ChatRoomRepository;
import com.project.nyang.modules.notification.service.NotificationService;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final ChatRoomRepository chatRoomRepository;

    @Operation(summary = "더미 채팅방 생성", description = "테스트용 채팅방 생성 (1번 유저, 2번 유저)")
    @PostMapping("/dummy-room")
    public ResponseEntity<Long> createDummyRoom() {
        ChatRoom room = ChatRoom.create(1L, 2L);
        return ResponseEntity.ok(chatRoomRepository.save(room).getId());
    }

    @Operation(summary = "미확인 채팅 알림 테스트", description = "더미 채팅방 생성 후 진행")
    @PostMapping("/chat-alert")
    public ResponseEntity<Void> createTestChatAlert(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User receiver = userDetails.getUser();
        ChatRoom dummyRoom = chatRoomRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("dummy room 필요"));

        notificationService.notifyUnreadChatMessage(
                receiver,
                "💬 테스트 메시지입니다",
                "/chat/room/" + dummyRoom.getId(),
                dummyRoom
        );

        return ResponseEntity.ok().build();
    }

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