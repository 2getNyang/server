package com.project.nyang.global.common.api;

import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.logging.RequestContext;
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
        String reqId = RequestContext.getRequestId();
        log.error("[{}] CustomException 발생 - 코드: {}, 메시지: {}",
                reqId, e.getCode(), e.getMessage());

        return ResponseEntity
                .status(e.getCode())
                .body(ApiErrorResponse.of(
                        reqId,  // RequestId 추가
                        e.getCode(),
                        e.getMessage(),
                        e.toString()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception e) {
        String reqId = RequestContext.getRequestId();
        log.error("[{}] Unhandled Exception 발생", reqId, e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiErrorResponse.of(
                        reqId,
                        500,
                        "서버 오류가 발생했습니다",
                        e.toString()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        String reqId = RequestContext.getRequestId();
        log.error("[{}] IllegalArgumentException 발생: {}", reqId, ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.of(
                        reqId,
                        400,
                        "잘못된 요청입니다: " + ex.getMessage(),
                        ex.toString()
                ));
    }
}