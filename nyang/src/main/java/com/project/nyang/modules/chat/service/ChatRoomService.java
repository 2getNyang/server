package com.project.nyang.modules.chat.service;

import com.project.nyang.modules.chat.entity.ChatRoom;
import com.project.nyang.modules.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 채팅방 서비스 클래스입니다.
 *
 * @author : 선순주
 * @fileName : ChatRoomService
 * @since : 2025-07-14
 */
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    /**
     * 두 사용자 간의 채팅방이 이미 존재하면 반환, 없으면 새로 생성
     */
    @Transactional
    public Long getOrCreateChatRoom(Long user1Id, Long user2Id) {
        // 정렬된 순서로 저장해야 유일한 키로 인식 가능
        Long first = Math.min(user1Id, user2Id);
        Long second = Math.max(user1Id, user2Id);

        Optional<ChatRoom> existing = chatRoomRepository.findByUser1IdAndUser2Id(first, second);

        if (existing.isPresent()) {
            return existing.get().getId();
        }

        ChatRoom newRoom = ChatRoom.builder()
                .user1Id(first)
                .user2Id(second)
                .build();

        return chatRoomRepository.save(newRoom).getId();
    }
}