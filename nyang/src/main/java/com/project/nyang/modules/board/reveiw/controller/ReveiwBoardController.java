package com.project.nyang.modules.board.reveiw.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.modules.board.reveiw.dto.ReveiwBoardDetailDTO;
import com.project.nyang.modules.board.reveiw.dto.ReveiwBoardListDTO;
import com.project.nyang.modules.board.reveiw.dto.ReviewBoardDTO;
import com.project.nyang.modules.board.reveiw.repository.ReviewBoardRepository;
import com.project.nyang.modules.board.reveiw.service.ReveiwBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 입양 후기 게시판 요청 받는 Controller
 *
 * @author : 박세정
 * @fileName : ReveiwBoardController
 * @since : 2025-07-08
 */
@Tag(name = "Review Board API", description = "입양 후기 게시판 관련 기능")
@RestController
@RequestMapping("api/v1/boards/2")
@RequiredArgsConstructor
public class ReveiwBoardController {

    // TODO. @AuthenticationPrincipal CustomUserDetails userDetails에서 userId를 추출하는 로직으로
    //          모든 api 수정 필요

    private final ReveiwBoardService reveiwBoardService;
    private final ReviewBoardRepository reviewBoardRepository;

    /**
     * 사용자가 입양 후기를 작성하는 기능
     */
    @Operation(summary = "입양 후기 게시글 등록")
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createReviewBoard(@RequestBody ReviewBoardDTO boardDTO) {
        reveiwBoardService.createReviewBoard(boardDTO);
        return ResponseEntity.ok(ApiSuccessResponse.success(null, "입양 후기 게시물 등록 완료"));
    }

    @Operation(summary = "입양 후기 게시글 리스트 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ReveiwBoardListDTO>>> getReviewBoards(@Parameter(description = "페이지 번호", example = "1") @RequestParam(defaultValue = "0") int page, @Parameter(description = "한 페이지에 보여줄 게시물 개수", example = "12") @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(ApiSuccessResponse.success(reveiwBoardService.getReviewBoards(page, size), "입양 후기 게시글 리스트 조회 성공"));
    }

    @Operation(summary = "입양 후기 게시글 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReveiwBoardDetailDTO>> getReviewBoardDetail(@Parameter(description = "게시물 ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(ApiSuccessResponse.success(reveiwBoardService.getReviewBoardDetail(id), "입양 후기 게시글 상세 조회 성공"));
    }

    @Operation(summary = "입양 후기 게시글 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> deleteReviewBoard(@Parameter(description = "게시물 ID", example = "1") @PathVariable Long id) {
        reveiwBoardService.deleteReviewBoard(id);
        return ResponseEntity.ok(ApiSuccessResponse.success(id, "입양 후기 게시물 등록 완료"));
    }

    @Operation(summary = "입양 후기 게시글 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updateReviewBoard(@Parameter(description = "게시물 ID", example = "1") @PathVariable Long id, @RequestBody ReviewBoardDTO boardDTO) {
        reveiwBoardService.updateReviewBoard(id, boardDTO);
        return ResponseEntity.ok(ApiSuccessResponse.success(null, "입양 후기 게시물 수정 완료"));
    }

//    @GetMapping("/search")
//    public ResponseEntity<?> searchReviewBoard(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "12") int size) {
//    }

}