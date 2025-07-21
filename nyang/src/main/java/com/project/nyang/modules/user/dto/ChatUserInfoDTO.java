package com.project.nyang.modules.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 채팅 상대방 dto
 *
 * @author : 선순주
 * @fileName : ChatUserInfoDTO
 * @since : 2025-07-17
 */
@Getter
@AllArgsConstructor
public class ChatUserInfoDTO {
        private Long userId;
        private String nickname;
}