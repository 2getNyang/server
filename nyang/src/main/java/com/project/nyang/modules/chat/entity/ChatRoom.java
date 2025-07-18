package com.project.nyang.modules.chat.entity;

import com.project.nyang.global.common.entity.BaseTime;
import com.project.nyang.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * 채팅방 엔티티
 *
 * @author : 이은서
 * @fileName : ChatRoom
 * @since : 25. 7. 7.
 */
@Entity
@Table(name = "CHAT_ROOM",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user1_id", "user2_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user1_id", nullable = false)
    private Long user1Id;

    @Column(name = "user2_id", nullable = false)
    private Long user2Id;

    public static ChatRoom create(Long userAId, Long userBId) {
        if (userAId.equals(userBId)) {
            throw new IllegalArgumentException("같은 사용자 간에는 채팅방을 만들 수 없습니다.");
        }

        Long user1 = Math.min(userAId, userBId);
        Long user2 = Math.max(userAId, userBId);

        return new ChatRoom(user1, user2);
    }

    private ChatRoom(Long user1Id, Long user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }
}