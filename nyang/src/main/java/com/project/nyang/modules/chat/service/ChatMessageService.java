package com.project.nyang.modules.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nyang.global.logging.LogMessage;
import com.project.nyang.modules.chat.dto.ChatMessageDTO;
import com.project.nyang.modules.chat.entity.ChatMessage;
import com.project.nyang.modules.chat.entity.ChatRoom;
import com.project.nyang.modules.chat.repository.ChatMessageRepository;
import com.project.nyang.modules.chat.repository.ChatRoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 채팅 관련 서비스입니다.
 *
 * @author : 선순주
 * @fileName : ChatMessageService
 * @since : 2025-07-14
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @LogMessage(value = "채팅방 메시지 전송", operation = "CREATE")
    public void saveMessage(ChatMessageDTO dto) {


        ChatRoom room = chatRoomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다."));

        ChatMessage message = new ChatMessage(room, dto.getSenderId(), dto.getContent());
        chatMessageRepository.save(message);
    }

    //안읽은 메시지 수 조회
    @LogMessage(value = "채팅방 안읽은 메시지 수 조회", operation = "READ")
    @Transactional(readOnly = true)
    public Long countUnreadMessages(Long roomId, Long userId) {
        return chatMessageRepository.countUnreadMessages(roomId, userId);
    }

    public List<ChatMessageDTO> getMessagesByRoomId(Long roomId) {
        List<ChatMessage> messages = chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
        return messages.stream()
                .map(ChatMessageDTO::fromEntity)
                .collect(Collectors.toList());
    }


    @Transactional
    public void markAllAsRead(Long roomId, Long userId) {
        chatMessageRepository.markMessagesAsRead(roomId, userId);
    }
    
    }




