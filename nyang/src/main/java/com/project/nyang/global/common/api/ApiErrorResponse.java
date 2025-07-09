package com.project.nyang.global.common.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public static ApiErrorResponse of(int code, String message, String detail) {
        return ApiErrorResponse.builder()
                .code(code)
                .message(message)
                .detail(detail)
                .build();
    }
}