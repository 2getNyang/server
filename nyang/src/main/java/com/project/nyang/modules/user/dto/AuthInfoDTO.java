package com.project.nyang.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.security.AuthProvider;

/**
 * 사용자 관련 dto입니다
 *
 * @author : 선순주
 * @fileName : AuthInfoDTO
 * @since : 2025-07-16
 */
@Getter
@Builder
@AllArgsConstructor
public class AuthInfoDTO {

    @Schema(description = "사용자 고유 ID", example = "1")
    private Long id;               // 사용자 고유 ID
    @Schema(description = "이메일", example = "eomlasticsearch@gmail.com")
    private String email;          // 사용자 이메일
    @Schema(description = "닉네임", example = "엄라스틱서치")
    private String nickname;       // 닉네임
    @Schema(description = "소셜 타입", example = "kakao")
    private String loginType;       //소셜 타입

}
