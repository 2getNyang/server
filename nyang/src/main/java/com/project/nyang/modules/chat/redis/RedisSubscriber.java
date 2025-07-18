package com.project.nyang.modules.chat.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.project.nyang.modules.chat.dto.ChatMessageDTO;
//import com.project.nyang.modules.chat.service.ChatMessageService;
import com.project.nyang.modules.notification.service.NotificationService;
import com.project.nyang.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author : 선순주
 * @fileName : RedisSubscriber
 * @since : 2025-07-14
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;
    //private final ChatMessageService chatMessageService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("📬 Redis 수신 메시지: {}", new String(message.getBody(), StandardCharsets.UTF_8));
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
//            ChatMessageDTO dto = objectMapper.readValue(body, ChatMessageDTO.class);
//
//            // 1. WebSocket으로 전달
//            messagingTemplate.convertAndSend("/sub/chat/" + dto.getRoomId(), dto);
//
//            // 2. DB 저장
//            chatMessageService.saveMessage(dto);

            // 3. 수신자 접속 여부 확인 → 미접속이면 알림 생성
//            if (!isUserInRoom(dto.getReceiverId(), dto.getRoomId())) {
//                User receiver = userRepository.findById(dto.getReceiverId())
//                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
//
//                String preview = dto.getMessage().length() > 20
//                        ? dto.getMessage().substring(0, 20) + "…"
//                        : dto.getMessage();
//
//                notificationService.notifyUnreadChatMessage(
//                        receiver,
//                        preview,
//                        "/chat/room/" + dto.getRoomId() // 🔗 알림 링크
//                );
//            }
        } catch (Exception e) {
            log.error("Redis 메시지 처리 오류", e);
        }
    }
}