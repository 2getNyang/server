package com.project.nyang.modules.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 읽음 구독 DTO
 *
 * @author : 선순주
 * @fileName : ChatReadDTO
 * @since : 2025-07-17
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatReadDTO {
    @Schema(description = "채팅방 ID", example = "501")
    private Long roomId;
    @Schema(description = "읽은 상대 사용자 ID", example = "25")
    private Long readerId; // 누가 읽었는지
}