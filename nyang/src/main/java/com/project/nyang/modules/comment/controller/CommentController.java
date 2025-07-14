package com.project.nyang.modules.comment.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.global.security.core.CustomUserDetails;
import com.project.nyang.modules.comment.dto.CreateCommentDTO;
import com.project.nyang.modules.comment.dto.UpdateCommentDTO;
import com.project.nyang.modules.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 댓글 관련 Controller
 *
 * @author : 박세정
 * @fileName : CommentController
 * @since : 2025-07-11
 */
@Tag(name = "COMMENT API", description = "댓글 관련 Controller")
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "게시글 댓글 등록")
    @PostMapping("/boards/{boardId}")
    public ResponseEntity<ApiResponse<Object>> createBoardComment(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody CreateCommentDTO dto, @Parameter(description = "게시글 ID") @PathVariable Long boardId) {
        Long userId = userDetails.getId();
        commentService.createBoardComment(userId, boardId, dto);
        return ResponseEntity.ok(ApiSuccessResponse.success(null, "게시글 댓글 등록 완료"));
    }
    
    @Operation(summary = "입양공고 댓글 등록")
    @PostMapping("/adoption/{desertionNo}")
    public ResponseEntity<ApiResponse<Object>> createAdoptionComment(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody CreateCommentDTO dto, @Parameter(description = "공고 번호") @PathVariable String desertionNo) {
        Long userId = userDetails.getId();
        commentService.createAdoptionComment(userId, desertionNo, dto);
        return ResponseEntity.ok(ApiSuccessResponse.success(null, "입양공고 댓글 등록 완료"));
    }
    

    @Operation(summary = "입양공고/게시글 댓글 수정")
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<UpdateCommentDTO>> updateComment(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UpdateCommentDTO dto, @Parameter(description = "댓글 ID") @PathVariable Long commentId) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(ApiSuccessResponse.success(commentService.updateComment(userId, commentId, dto), "게시글 댓글 수정 완료"));
    }

    @Operation(summary = "입양공고/게시글 댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Object>> deleteComment(@AuthenticationPrincipal CustomUserDetails userDetails, @Parameter(description = "댓글 ID") @PathVariable Long commentId) {
        Long userId = userDetails.getId();
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.ok(ApiSuccessResponse.success(null, "게시글 삭제 성공: " + commentId));
    }

}