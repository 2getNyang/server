package com.project.nyang.modules.chat.dto;

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
    private Long roomId;
    private String opponentNickname;
    private String lastMessageContent;
    private LocalDateTime lastMessageTime;
    private int unreadCount;
}