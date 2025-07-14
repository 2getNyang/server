package com.project.nyang.modules.mypage.controller;

import com.project.nyang.global.common.api.ApiErrorResponse;
import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.global.security.core.CustomUserDetails;
import com.project.nyang.modules.mypage.dto.MyAnimalListDTO;
import com.project.nyang.modules.mypage.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 마이페이지 controller
 *
 * @author : 박세정
 * @fileName : MyPageController
 * @since : 2025-07-12
 */
@Tag(name = "Mypage API", description = "마이페이지 Controller")
@RestController
@RequestMapping("/api/v1/my")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "찜한 입양공고 리스트 조회")
    @GetMapping("/bookmarks")
    public ResponseEntity<ApiResponse<Page<MyAnimalListDTO>>> getMyAnimals(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(ApiSuccessResponse.success(myPageService.getMyAnimals(userId, page, size), "게시글 댓글 등록 완료"));
    }

}