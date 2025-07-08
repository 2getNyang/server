package com.project.nyang.global.exception;

import com.project.nyang.global.exception.ErrorCode;

/**
 * CustomException 클래스입니다.
 *
 * @author : 선순주
 * @fileName : CustomException
 * @since : 2025-07-08
 */
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 부모 예외에 메시지 전달
        this.errorCode = errorCode;
    }

    public int getCode() {
        return errorCode.getCode();
    }
    public String getErrorMessage() {
        return errorCode.getMessage();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}