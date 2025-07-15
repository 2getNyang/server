package com.project.nyang.modules.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long senderId;
    private String content;
    private Boolean isRead = false;
}
