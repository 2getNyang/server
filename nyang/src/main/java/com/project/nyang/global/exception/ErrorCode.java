package com.project.nyang.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum 타입 ErrorCod입니다.
 *
 * @author : 선순주
 * @fileName : ErrorCode
 * @since : 2025-07-08
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(400, "잘못된 요청입니다."),
    INVALID_PASSWORD(401, "비밀번호가 맞지않습니다"),
    INVALID_EMAIL(401, "등록된 이메일이 없습니다."),
    DUPLICATE_EMAIL(401, "이미 등록된 이메일입니다."),
    UNAUTHORIZED(401, "인증이 필요합니다."),
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생했습니다."),
    UNAUTHORIZED_REQUEST(401, "권한이 없습니다."),
    //토큰 관련 에러
    REFRESH_TOKEN_MISSING(401, "리프레시 토큰이 없습니다."),
    REFRESH_TOKEN_INVALID(401, "유효하지 않은 리프레시 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(404, "리프레시 토큰을 찾을 수 없습니다."),

    //게시글 관련 에러
    BOARD_NOT_FOUND(404, "게시글을 찾을 수 없습니다.");

    private final int code;
    private final String message;
}