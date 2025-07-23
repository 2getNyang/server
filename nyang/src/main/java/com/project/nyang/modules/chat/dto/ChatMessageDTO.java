package com.project.nyang.modules.chat.dto;

import com.project.nyang.modules.chat.entity.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "채팅방 ID", example = "101")
    private Long roomId;

    @Schema(description = "메시지 ID", example = "501")
    private Long id;

    @Schema(description = "보낸 사람의 사용자 ID", example = "42")
    private Long senderId;

    @Schema(description = "메시지 내용", example = "안녕하세요!")
    private String content;

    @Schema(description = "메시지 읽음 여부", example = "false")
    private Boolean isRead = false;

    @Schema(description = "메시지 작성 시각", example = "2025-07-14T12:34:56")
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
