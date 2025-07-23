package com.project.nyang.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "채팅 상대 사용자 고유 ID", example = "1")
        private Long userId;
        @Schema(description = "닉네임", example = "엄라스틱서치")
        private String nickname;
}