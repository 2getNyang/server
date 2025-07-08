package com.project.nyang.global.common.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 성공 API 공통응답 클래스입니다.
 *
 * @author : 선순주
 * @fileName : ApiSuccessResponse
 * @since : 2025-07-08
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "공통 성공 응답 포맷")
public class ApiSuccessResponse<T>  {
    private int code;
    private T data;
    private String message;

    //성공 응답 : code, data, message
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .code(200)
                .data(data)
                .message(message)
                .build();
    }

    //성공 응답 : data, message
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "요청이 성공적으로 처리되었습니다");
    }

}