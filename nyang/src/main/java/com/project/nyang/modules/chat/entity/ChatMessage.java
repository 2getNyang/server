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
//@Entity
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "CHAT_MSG")
//public class ChatMessage extends BaseTime {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "chat_msg_id")
//    private Long chatMsgId;
//
//    @Lob
//    @Column(name = "chat_content", nullable = false)
//    private String chatContent;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "chat_room_id", nullable = false)
//    private ChatRoom chatRoom;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @Builder
//    public ChatMessage(String chatContent, ChatRoom chatRoom, User user) {
//        this.chatContent = chatContent;
//        this.chatRoom = chatRoom;
//        this.user = user;
//    }
//}
