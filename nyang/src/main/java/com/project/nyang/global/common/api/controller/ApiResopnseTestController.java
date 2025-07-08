package com.project.nyang.global.common.api.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 공통응답 테스트용 컨트롤러 입니다.
 *
 * @author : 선순주
 * @fileName : ApiResopnseTestController
 * @since : 2025-07-08
 */
@RestController
@RequestMapping("/api/test")
public class ApiResopnseTestController {
    @GetMapping("/success")
    public ResponseEntity<ApiResponse<String>> testSuccess() {
        String result = "정상 처리됨!";
        return ResponseEntity.ok(ApiSuccessResponse.success(result, "성공 메시지입니다"));
    }

    @GetMapping("/fail")
    public ResponseEntity<ApiResponse<Void>> testFail() {
        throw new CustomException(ErrorCode.INVALID_PASSWORD);
    }
}