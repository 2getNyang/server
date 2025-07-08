package com.project.nyang.global.common.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Error관련 API 공통응답 클래스입니다.
 *
 * @author : 선순주
 * @fileName : ApiErrorResponse
 * @since : 2025-07-08
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "공통 에러 응답 포맷")
public class ApiErrorResponse {
    @Schema(description = "에러 코드", example = "401")
    private int code;
    @Schema(description = "에러 메시지", example = "비밀번호가 맞지 않습니다")
    private String message;
    @Schema(description = "에러 상세 메시지", example = "CustomException: 비밀번호가 맞지 않습니다")
    private String detail;

    //예외 응답 :  code, data, message
    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .data(null)
                .message(message)
                .build();
    }
}