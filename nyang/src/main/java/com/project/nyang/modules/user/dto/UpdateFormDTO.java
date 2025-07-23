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
    @Schema(description = "닉네임", example = "엄라스틱서치")
    private String nickname;

    @Schema(description = "이메일", example = "eomlasticsearch@gmail.com")
    private String email;
}