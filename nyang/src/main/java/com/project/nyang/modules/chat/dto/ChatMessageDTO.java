package com.project.nyang.modules.chat.dto;

import com.project.nyang.modules.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 채팅 메시지 관련 DTO입니다.
 *
 * @author : 선순주
 * @fileName : ChatMessageDTO
 * @since : 2025-07-14
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private Long roomId;
    private Long id;
    private Long senderId;
    private String content;
    private Boolean isRead = false;
    private LocalDateTime createdAt;

    public static ChatMessageDTO fromEntity(ChatMessage entity) {
        return new ChatMessageDTO(
                entity.getRoom().getId(),
                entity.getId(),
                entity.getSenderId(),
                entity.getContent(),
                entity.getIsRead(),
                entity.getCreatedAt()
        );
    }
}
