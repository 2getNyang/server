package com.project.nyang.modules.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nyang.modules.chat.dto.ChatMessageDTO;
import com.project.nyang.modules.chat.redis.RedisPublisher;
import com.project.nyang.modules.chat.service.ChatMessageService;
import com.project.nyang.modules.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

/**
 * @author : 선순주
 * @fileName : ChatController
 * @since : 2025-07-14
 */
@RequiredArgsConstructor
@Controller
@Slf4j
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ObjectMapper objectMapper;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @PostMapping("/chat-room")
    public ResponseEntity<Long> createRoom(@RequestParam Long user1Id, @RequestParam Long user2Id) {
        Long roomId = chatRoomService.getOrCreateChatRoom(user1Id, user2Id);
        return ResponseEntity.ok(roomId);
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message) throws JsonProcessingException {
        log.info("\uD83D\uDCE5받은 메시지: {}", message);
        String topic = "chatroom." + message.getRoomId();
        String jsonMessage = objectMapper.writeValueAsString(message);
        redisPublisher.publish(topic, jsonMessage);
    }

    @MessageMapping("/chat/read/{roomId}")
    public void read(@DestinationVariable Long roomId, @Header("userId") Long userId) {
        chatMessageService.markAllAsRead(roomId, userId);
    }
}