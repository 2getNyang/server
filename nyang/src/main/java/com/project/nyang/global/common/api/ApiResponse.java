package com.project.nyang.global.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 공통응답 API Response 입니다.
 *
 * @author : 선순주
 * @fileName : ApiResponse
 * @since : 2025-07-08
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {

    private int code;
    private T data;
    private String message;



}