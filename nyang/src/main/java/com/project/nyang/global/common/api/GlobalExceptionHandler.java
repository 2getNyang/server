package com.project.nyang.global.common.api;

import com.project.nyang.global.common.api.ApiErrorResponse;
import com.project.nyang.global.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiErrorResponse> handleCustom(CustomException e) {
        log.warn("CustomException 발생: {}", e.getMessage());
        return ResponseEntity
                .status(e.getCode())
                .body(ApiErrorResponse.of(e.getCode(), e.getMessage(), e.toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception e) {
        log.error("Unhandled Exception", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiErrorResponse.of(500, "서버 오류가 발생했습니다", e.toString()));
    }
}