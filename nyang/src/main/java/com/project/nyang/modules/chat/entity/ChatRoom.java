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
//@Entity
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "CHAT_ROOM")
//public class ChatRoom extends BaseTime {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "chat_room_id")
//    private Long chatRoomId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user1;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user2;
//
//    @Builder
//    public ChatRoom(User user1, User user2) {
//        this.user1 = user1;
//        this.user2 = user2;
//    }
//}
