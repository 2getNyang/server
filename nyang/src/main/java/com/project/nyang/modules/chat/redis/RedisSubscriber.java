package com.project.nyang.modules.chat.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.modules.chat.dto.ChatMessageDTO;
import com.project.nyang.modules.chat.entity.ChatRoom;
import com.project.nyang.modules.chat.repository.ChatRoomRepository;
import com.project.nyang.modules.chat.service.ChatMessageService;
import com.project.nyang.modules.notification.service.NotificationService;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import com.project.nyang.modules.notification.service.NotificationService;
import com.project.nyang.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
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
    private final ChatMessageService chatMessageService;
    private final NotificationService notificationService;

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpUserRegistry simpUserRegistry;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("📬 Redis 수신 메시지: {}", new String(message.getBody(), StandardCharsets.UTF_8));
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
            ChatMessageDTO dto = objectMapper.readValue(body, ChatMessageDTO.class);

            // 1. WebSocket으로 전달
            messagingTemplate.convertAndSend("/sub/chat/" + dto.getRoomId(), dto);

            // 2. DB 저장
            chatMessageService.saveMessage(dto);

            // 3. 채팅방 조회
            ChatRoom room = chatRoomRepository.findById(dto.getRoomId())
                    .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

            // 4. 수신자 확인
            Long receiverId = room.getUser1Id().equals(dto.getSenderId())
                    ? room.getUser2Id() : room.getUser1Id();

            // 5. 수신자 접속 여부 확인 → 미접속이면 알림 생성
            if (!isUserInRoom(receiverId, room.getId())) {
                User receiver = userRepository.findById(receiverId)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

                String preview = dto.getContent().length() > 20
                        ? dto.getContent().substring(0, 20) + "…"
                        : dto.getContent();

                notificationService.notifyUnreadChatMessage(
                        receiver,
                        preview,
                        "/chat/room/" + room.getId(),
                        room // ChatRoom 객체 전달
                );
            }
        } catch (Exception e) {
            log.error("Redis 메시지 처리 오류", e);
        }
    }

    private boolean isUserInRoom(Long userId, Long roomId) {
        String destination = "/sub/chat/" + roomId;

        for (SimpUser user : simpUserRegistry.getUsers()) {
            if (user.getName().equals(userId.toString())) { // userId와 STOMP 세션의 username 매핑
                return user.getSessions().stream()
                        .flatMap(session -> session.getSubscriptions().stream())
                        .anyMatch(sub -> sub.getDestination().equals(destination));
            }
        }
        return false;
    }
}