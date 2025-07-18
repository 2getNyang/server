package com.project.nyang.modules.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.modules.chat.dto.ChatMessageDTO;
import com.project.nyang.modules.chat.dto.ChatReadDTO;
import com.project.nyang.modules.chat.redis.RedisPublisher;
import com.project.nyang.modules.chat.service.ChatMessageService;
import com.project.nyang.modules.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

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
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/api/v1/chat/room")
    public ResponseEntity<Long> createRoom(@RequestParam Long user1Id, @RequestParam Long user2Id) {
        Long roomId = chatRoomService.getOrCreateChatRoom(user1Id, user2Id);
        return ResponseEntity.ok(roomId);
    }

    @GetMapping("/api/v1/chat/room/{roomId}/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageDTO>>> getMessagesByRoom(
            @PathVariable Long roomId
    ) {
        List<ChatMessageDTO> messages = chatMessageService.getMessagesByRoomId(roomId);
        return ResponseEntity.ok(ApiSuccessResponse.success(messages, "채팅 메시지 조회 성공"));
    }


    @MessageMapping("/api/v1/chat/message")
    public void message(ChatMessageDTO message) throws JsonProcessingException {
        log.info("\uD83D\uDCE5받은 메시지: {}", message);
        String topic = "chatroom." + message.getRoomId();
        String jsonMessage = objectMapper.writeValueAsString(message);
        redisPublisher.publish(topic, jsonMessage);
    }

    @MessageMapping("/api/v1/chat/read/{roomId}")
    public void read(@DestinationVariable Long roomId, @Header("userId") Long userId) {
        chatMessageService.markAllAsRead(roomId, userId);


        // 상대방에게 읽음 알림 전송
        ChatReadDTO readInfo = new ChatReadDTO(roomId, userId); // DTO는 아래 참고
        messagingTemplate.convertAndSend("/sub/chat/" + roomId + "/read", readInfo);
    }
    }
