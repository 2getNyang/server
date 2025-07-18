package com.project.nyang.modules.notification.entity;

import com.project.nyang.modules.chat.entity.ChatRoom;
import com.project.nyang.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 *
 * Notification Entity
 * @fileName        : Notification
 * @author          : 이지은
 * @since           : 2025-07-07
 *
 */

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "NOTIFICATION")
public class Notification{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noty_id")
    private Long notyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "noty_type")
    private NotificationType type;

    @Column(name = "noty_content", columnDefinition = "TEXT")
    private String notyContent;

    @Column(name = "noty_link")
    private String notyLink;

    @Column(name = "noty_isread")
    private Boolean isRead = false;

    @CreatedDate
    @Column(name = "noty_created_at", columnDefinition = "TIMESTAMP",
            updatable = false, nullable = false)
    private LocalDateTime notyCreatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    public enum NotificationType {
        CHAT_REPLY, FORM_SENT//, ETC 알림메세지 기타로 처리할게 있을까..?
    }


     @Builder
     public Notification(
             NotificationType type,
             String notyContent,
             String notyLink,
             Boolean isRead,
             ChatRoom chatRoom,
             User user
     ) {
         this.type = type;
         this.notyContent = notyContent;
         this.notyLink = notyLink;
         this.isRead = isRead;
         this.chatRoom = chatRoom;
         this.user = user;
     }

    public void markAsRead() {
        this.isRead = true;
    }
}