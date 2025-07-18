package com.project.nyang.modules.chat.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nyang.modules.chat.dto.ChatMessageDTO;
import com.project.nyang.modules.chat.service.ChatMessageService;
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
    private final ChatMessageService chatMessageService;

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
        } catch (Exception e) {
            log.error("Redis 메시지 처리 오류", e);
        }
    }
}