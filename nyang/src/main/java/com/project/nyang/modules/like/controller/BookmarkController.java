package com.project.nyang.modules.like.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.global.security.core.CustomUserDetails;
import com.project.nyang.modules.like.dto.LikeDto;
import com.project.nyang.modules.like.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 입양 공고 찜 Controller
 *
 * @author : 박세정
 * @fileName : BookmarkController
 * @since : 2025-07-11
 */
@Tag(name = "BOOKMARK API", description = "찜 관련 기능")
@RestController
@RequestMapping("/api/v1/bookmark")
@RequiredArgsConstructor
public class BookmarkController {
    private final LikeService likeService;

    @Operation(summary = "입양 공고 찜 추가")
    @PostMapping("/{desertionNo}")
    public ResponseEntity<ApiResponse<LikeDto>> createBookmark(@AuthenticationPrincipal CustomUserDetails userDetails, @Parameter(description = "공고 번호") @PathVariable String desertionNo) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(ApiSuccessResponse.success(likeService.createBookmark(userId, desertionNo), "찜하기 성공"));
    }

    @Operation(summary = "입양 공고 찜 삭제")
    @DeleteMapping("/{desertionNo}")
    public ResponseEntity<ApiResponse<LikeDto>> deleteBookmark(@AuthenticationPrincipal CustomUserDetails userDetails, @Parameter(description = "공고 번호") @PathVariable String desertionNo) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(ApiSuccessResponse.success(likeService.deleteBookmark(userId, desertionNo), "찜 취소 성공"));
    }

}