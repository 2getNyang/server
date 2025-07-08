package com.project.nyang.global.common.api;

import com.project.nyang.global.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.project.nyang.global.common.api.ApiErrorResponse;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * GlobalExceptionHandler 에러처리 클래스입니다.
 *
 * @author : 선순주
 * @fileName : GlobalExceptionHandler
 * @since : 2025-07-08
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 개발자가 정의한 CustomException 처리
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomException(CustomException e) {
        log.warn("CustomException 발생: {}", e.getErrorMessage());

        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(e.getCode())
                .message(e.getErrorMessage())
                .detail(e.toString())
                .build();

        return ResponseEntity.status(e.getCode()).body(response);
    }

    /**
     * 처리되지 않은 모든 예외 처리 (서버 에러)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception e) {
        log.error("Unhandled Exception 발생", e);

        ApiErrorResponse response = ApiErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("서버 오류가 발생했습니다")
                .detail(e.toString())  // ex) java.lang.NullPointerException
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}