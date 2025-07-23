package com.project.nyang.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * 업데이트 수정폼 DTO입니다
 *
 * @author : 선순주
 * @fileName : UpdateFormDTO
 * @since : 2025-07-21
 */
@Getter
public class UpdateFormDTO {
    @Schema(description = "사용자 닉네임", example = "고양이사랑")
    private String nickname;

    @Schema(description = "사용자 이메일", example = "meowmeow@example.com")
    private String email;
}