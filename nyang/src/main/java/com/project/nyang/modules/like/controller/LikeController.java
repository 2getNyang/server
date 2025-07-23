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
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 좋아요 Controller
 *
 * @author : 박세정
 * @fileName : LikeController
 * @since : 2025-07-10
 */
@Tag(name = "LIKE API", description = "좋아요 관련 기능")
@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "게시글 좋아요 추가")
    @PostMapping("/{boardId}")
    public ResponseEntity<ApiResponse<LikeDto>> createLike(@AuthenticationPrincipal CustomUserDetails userDetails, @Parameter(description = "게시글 ID", example = "1") @PathVariable Long boardId) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(ApiSuccessResponse.success(likeService.createLike(userId, boardId), "좋아요 성공"));
    }

    @Operation(summary = "게시글 좋아요 삭제")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<LikeDto>> deleteLike(@AuthenticationPrincipal CustomUserDetails userDetails, @Parameter(description = "게시글 ID", example = "1") @PathVariable Long boardId) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(ApiSuccessResponse.success(likeService.deleteLike(userId, boardId), "좋아요 취소 성공"));
    }

    @Operation(summary = "게시글 좋아요 카운트")
    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Long>> countLike(@Parameter(description = "게시글 ID", example = "1") @PathVariable Long boardId) {
        Long count = likeService.countLike(boardId);
        return ResponseEntity.ok(ApiSuccessResponse.success(count, "좋아요 수 조회 성공"));
    }

    @Operation(summary = "게시글 좋아요 여부 확인")
    @GetMapping("/{boardId}/me")
    public ResponseEntity<ApiResponse<Boolean>> checkLike(@AuthenticationPrincipal CustomUserDetails userDetails, @Parameter(description = "게시글 ID", example = "1") @PathVariable Long boardId){
        Long userId = userDetails.getId();
        boolean hasLiked = likeService.hasUserLikedBoard(userId, boardId);
        return ResponseEntity.ok(ApiSuccessResponse.success(hasLiked));
    }
}