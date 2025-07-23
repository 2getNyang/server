package com.project.nyang.modules.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 1:1 채팅 내역 dto
 *
 * @author : 선순주
 * @fileName : ChatRoomSummaryDTO
 * @since : 2025-07-21
 */

@Getter
@Builder
public class ChatRoomSummaryDTO {
    @Schema(description = "채팅방 ID", example = "101")
    private Long roomId;

    @Schema(description = "상대방 닉네임", example = "고양이집사")
    private String opponentNickname;

    @Schema(description = "마지막 메시지 내용", example = "입양 문의드립니다!")
    private String lastMessageContent;

    @Schema(description = "마지막 메시지 시간", example = "2025-07-23T14:22:00")
    private LocalDateTime lastMessageTime;

    @Schema(description = "읽지 않은 메시지 수", example = "3")
    private int unreadCount;
}