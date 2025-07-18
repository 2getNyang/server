package com.project.nyang.modules.chat.repository;

import com.project.nyang.modules.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 채팅방 레포지토리입니다.
 *
 * @author : 선순주
 * @fileName : ChatRoomRepository
 * @since : 2025-07-14
 */
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);
}
