package com.project.nyang.modules.chat.service;

import com.project.nyang.modules.chat.dto.ChatRoomSummaryDTO;
import com.project.nyang.modules.chat.entity.ChatMessage;
import com.project.nyang.modules.chat.entity.ChatRoom;
import com.project.nyang.modules.chat.repository.ChatMessageRepository;
import com.project.nyang.modules.chat.repository.ChatRoomRepository;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

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

    public List<ChatRoomSummaryDTO> getUserChatRooms(Long currentUserId) {
        List<ChatRoom> rooms = chatRoomRepository.findChatRoomsByUser(currentUserId);

        return rooms.stream()
                .map(room -> {
                    // 1. 상대방 ID 추출
                    Long opponentId = room.getUser1Id().equals(currentUserId)
                            ? room.getUser2Id()
                            : room.getUser1Id();

                    User opponent = userRepository.findById(opponentId)
                            .orElseThrow(() -> new EntityNotFoundException("상대 유저를 찾을 수 없습니다."));

                    // 2. 마지막 메시지
                    ChatMessage lastMessage = chatMessageRepository
                            .findTopByRoomOrderByCreatedAtDesc(room)
                            .orElse(null);

                    // 3. 안 읽은 메시지 수
                    int unreadCount = chatMessageRepository
                            .countByRoomAndSenderIdNotAndIsReadFalse(room, currentUserId);

                    // 4. DTO 반환
                    return ChatRoomSummaryDTO.builder()
                            .roomId(room.getId())
                            .opponentNickname(opponent.getNickname())
                            .lastMessageContent(lastMessage != null ? lastMessage.getContent() : "")
                            .lastMessageTime(lastMessage != null ? lastMessage.getCreatedAt() : null)
                            .unreadCount(unreadCount)
                            .build();
                })
                .sorted(Comparator.comparing(
                        ChatRoomSummaryDTO::getLastMessageTime,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

}