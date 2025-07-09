package com.project.nyang.global.common.api;

import com.project.nyang.global.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "공통 응답 포맷")
public class ApiSuccessResponse<T> {
    @Schema(description = "응답 코드", example = "200")
    private int code;

    @Schema(description = "응답 데이터")
    private T data;

    @Schema(description = "메시지", example = "요청이 성공적으로 처리되었습니다.")
    private String message;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .code(200)
                .data(data)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(data, "요청이 성공적으로 처리되었습니다.");
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .data(null)
                .message(message)
                .build();
    }
}