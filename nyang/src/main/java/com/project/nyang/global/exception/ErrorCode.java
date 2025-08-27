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
    DUPLICATE_NICKNAME(401, "이미 등록된 닉네임입니다."),
    USER_NOT_FOUND(404,"찾을 수 없는 유저입니다."),

    //토큰 관련 에러
    REFRESH_TOKEN_MISSING(401, "리프레시 토큰이 없습니다."),
    REFRESH_TOKEN_INVALID(401, "유효하지 않은 리프레시 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(404, "리프레시 토큰을 찾을 수 없습니다."),
    GOOGLE_REVOKE_FAILED(500,"구글 계정 연결 해제에 실패했습니다"),

    //게시글 관련 에러
    BOARD_NOT_FOUND(404, "게시글을 찾을 수 없습니다."),
    BOARD_ALLREDAY_DELETE(401, "삭제된 게시글입니다."),
    FORBIDDEN(403, "권한이 없습니다."),
    CATEGORY_NOT_FOUND(404,"잘못된 카테고리입니다"),

    //보호소 관련 에러
    SHELTER_NOT_FOUND(404, "해당 보호소가 존재하지 않습니다."),

    //동물 관련 에러
    INVALID_ANIMAL(404, "해당 동물을 찾을 수 없습니다."),
    INVALID_NOTICE_DATE(401, "시작일이 종료일보다 이후여야 합니다."),
    INVALID_DATE(401, "시작일과 종료일은 모두 입력하거나 모두 비워야 합니다."),

    //입양 신청 내역 에러
    APPLICATION_NOT_FOUND(404, "입양 신청 내역을 찾을 수 없습니다."),
    INVALID_USER(404,"해당 유저를 찾을 수 없습니다."),
    PDF_GENERATION_FAILED(500,"PDF 변환에 실패하였습니다."),
    FONT_PATH_FAILED(500, "폰트 경로 변환 실패"),
    FONT_FOUND_FAILED(500,"폰트 파일을 찾을 수 없습니다."),
    SHELTER_EMAIL_NOT_FOUND(404,"보호소 이메일을 찾을 수 없습니다."),
    INVALID_TEMPLATE(404,"입양신청서 템플릿 파일을 찾을 수 없습니다."),
    MAIL_SEND_FAIL(500,"메일 전송에 실패하였습니다."),

    //알림 관련 에러
    NOTIFICATION_NOT_FOUND(404,"관련 알림을 찾을 수 없습니다."),
    CHATROOM_NOT_FOUND(404,"채팅방을 찾을 수 없습니다.");
    private final int code;
    private final String message;
}