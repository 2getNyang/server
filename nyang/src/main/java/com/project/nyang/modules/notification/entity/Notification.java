package com.project.nyang.modules.notification.entity;

import com.project.nyang.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "noty_type")
    private NotificationType type;

    @Column(name = "noty_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "noty_link")
    private String link;

    @Column(name = "noty_isread")
    private Boolean isRead = false;

    @CreatedDate
    @Column(name = "form_created_at", columnDefinition = "TIMESTAMP",
            updatable = false, nullable = false)
    private LocalDateTime formCreatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "chat_room_id", nullable = false)
//    private ChatRoom chatRoom;

    public enum NotificationType {
        CHAT_REPLY, FORM_SENT//, ETC 알림메세지 기타로 처리할게 있을까..?
    }


    //전에 builder 패턴 사용하기로 했어서 임시 추가
    // @Builder
    // public Notification(
    //     NotificationType type,
    //     String content,
    //     String link,
    //     Boolean isRead,
    //     LocalDateTime createdAt,
    //     User user,
    //     ChatRoom chatRoom
    // ) {
    //     this.type = type;
    //     this.content = content;
    //     this.link = link;
    //     this.isRead = isRead;
    //     this.createdAt = createdAt;
    //     this.user = user;
    //     this.chatRoom = chatRoom;
    // }
}