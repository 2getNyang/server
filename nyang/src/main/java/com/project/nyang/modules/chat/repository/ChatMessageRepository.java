package com.project.nyang.modules.chat.repository;

import com.project.nyang.modules.chat.entity.ChatMessage;
import com.project.nyang.modules.chat.entity.ChatRoom;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 채팅 메시지 관련 레포지토리입니다.
 *
 * @author : 선순주
 * @fileName : ChatMessageRepository
 * @since : 2025-07-14
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByRoom_IdOrderByCreatedAt(Long roomId);

    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.room.id = :roomId AND m.senderId <> :userId AND m.isRead = false")
    Long countUnreadMessages(@Param("roomId") Long roomId, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE ChatMessage m SET m.isRead = true WHERE m.room.id = :roomId AND m.senderId <> :userId AND m.isRead = false")
    void markMessagesAsRead(@Param("roomId") Long roomId, @Param("userId") Long userId);

    List<ChatMessage> findByRoomIdOrderByCreatedAtAsc(Long roomId);
    Optional<ChatMessage> findTopByRoomOrderByCreatedAtDesc(ChatRoom room);
//    int countByRoomAndSenderIdNotAndIsReadFalse(ChatRoom room, Long currentUserId);
    int countByRoomAndSenderIdNotAndIsReadFalse(ChatRoom room, Long senderId);
}