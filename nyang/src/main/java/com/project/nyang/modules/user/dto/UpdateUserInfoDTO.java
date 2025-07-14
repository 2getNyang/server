package com.project.nyang.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UpdateUserInfoDTO입니다.
 *
 * @author : 엄아영
 * @fileName : UpdateUserInfoDTO
 * @since : 2025-07-13
 */

@Data
@NoArgsConstructor
public class UpdateUserInfoDTO {
    @Schema(description = "사용자 닉네임", example = "고양이사랑")
    private String nickname;

    @Schema(description = "사용자 이메일", example = "meowmeow@example.com")
    private String email;
}