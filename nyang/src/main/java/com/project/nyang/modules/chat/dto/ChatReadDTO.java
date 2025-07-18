package com.project.nyang.modules.chat.dto;

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
    private Long roomId;
    private Long readerId; // 누가 읽었는지
}