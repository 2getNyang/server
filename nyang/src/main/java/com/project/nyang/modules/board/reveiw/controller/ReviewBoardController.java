package com.project.nyang.modules.board.reveiw.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.global.security.core.CustomUserDetails;
import com.project.nyang.modules.board.reveiw.dto.ReveiwBoardDetailDTO;
import com.project.nyang.modules.board.reveiw.dto.ReveiwBoardListDTO;
import com.project.nyang.modules.board.reveiw.dto.ReviewBoardCreateDTO;
import com.project.nyang.modules.board.reveiw.dto.ReviewBoardUpdateDTO;
import com.project.nyang.modules.board.reveiw.service.ReveiwBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 입양 후기 게시판 요청 받는 Controller
 *
 * @author : 박세정
 * @fileName : ReveiwBoardController
 * @since : 2025-07-08
 */
@Tag(name = "Review Board API", description = "입양 후기 게시판 관련 기능")
@RestController
@RequestMapping("/api/v1/boards/review")
@RequiredArgsConstructor
public class ReviewBoardController {

    private final ReveiwBoardService reveiwBoardService;

    /**
     * 사용자가 입양 후기를 작성하는 기능
     */
    @Operation(summary = "입양 후기 게시글 등록")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<Object>> createReviewBoard(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @RequestPart ReviewBoardCreateDTO boardDTO,
                                                                 @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        Long userId = userDetails.getId();
        reveiwBoardService.createReviewBoard(userId, boardDTO, images);
        return ResponseEntity.ok(ApiSuccessResponse.success(null, "입양 후기 게시물 등록 완료"));
    }

    @Operation(summary = "입양 후기 게시글 리스트 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ReveiwBoardListDTO>>> getReviewBoards(@Parameter(description = "페이지 번호", example = "0") @RequestParam(defaultValue = "0") int page, @Parameter(description = "한 페이지에 보여줄 게시물 개수", example = "12") @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(ApiSuccessResponse.success(reveiwBoardService.getReviewBoards(page, size), "입양 후기 게시글 리스트 조회 성공"));
    }

    @Operation(summary = "입양 후기 게시글 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReveiwBoardDetailDTO>> getReviewBoardDetail(@Parameter(description = "게시물 ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(ApiSuccessResponse.success(reveiwBoardService.getReviewBoardDetail(id), "입양 후기 게시글 상세 조회 성공"));
    }

    @Operation(summary = "입양 후기 게시글 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> deleteReviewBoard(@AuthenticationPrincipal CustomUserDetails userDetails, @Parameter(description = "게시물 ID", example = "1") @PathVariable Long id) {
        Long userId = userDetails.getId();
        reveiwBoardService.deleteReviewBoard(id, userId);
        return ResponseEntity.ok(ApiSuccessResponse.success(id, "입양 후기 게시물 삭제 완료"));
    }

    @Operation(summary = "입양 후기 게시글 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updateReviewBoard(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @Parameter(description = "게시물 ID", example = "1") @PathVariable Long id,
                                                                 @RequestPart ReviewBoardUpdateDTO boardDTO,
                                                                 @RequestPart(required = false) List<MultipartFile> newImages) {
        Long userId = userDetails.getId();
        reveiwBoardService.updateReviewBoard(userId, id, boardDTO, newImages);
        return ResponseEntity.ok(ApiSuccessResponse.success(null, "입양 후기 게시물 수정 완료"));
    }

//    @GetMapping("/search")
//    public ResponseEntity<?> searchReviewBoard(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
//    }

}