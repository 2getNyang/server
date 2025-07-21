package com.project.nyang.modules.chat.entity;

import com.project.nyang.global.common.entity.BaseTime;
import com.project.nyang.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * 채팅 메시지 엔티티
 *
 * @author : 이은서
 * @fileName : ChatMessage
 * @since : 25. 7. 7.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "CHAT_Message")
public class ChatMessage extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom room;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    public ChatMessage(ChatRoom room, Long senderId, String content) {
        this.room = room;
        this.senderId = senderId;
        this.content = content;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}

