package com.project.nyang.modules.user.dto;

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

    private Long id;               // 사용자 고유 ID
    private String email;          // 사용자 이메일
    private String nickname;       // 닉네임
    private String loginType;       //소셜 타입

}
