package com.project.nyang.modules.board.elasticsearch.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.global.security.core.CustomUserDetails;
import com.project.nyang.modules.board.elasticsearch.dto.BoardListDTO;
import com.project.nyang.modules.board.elasticsearch.service.BoardEsService;
import com.project.nyang.modules.board.review.dto.ReveiwBoardListDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * ElasticSearch 관련 controller
 *
 * @author : 박세정
 * @fileName : BoardEsController
 * @since : 2025-07-15
 */
@Tag(name = "Board elastic search API", description = "게시글 elastic search 관련 기능")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/boards")
public class BoardEsController {

    private final BoardEsService boardEsService;
    private static final Long REVIEW_CATEGORY_ID = 2L;
    private static final Long SNS_CATEGORY_ID = 3L;
    private static final Long LOST_CATEGORY_ID = 4L;

    @Operation(summary = "입양 후기 게시글 검색")
    @GetMapping("/review/elasticsearch")
    public ResponseEntity<ApiResponse<Page<BoardListDTO>>> searchReviewBoard(
            @Parameter(description = "검색어", example = "제목") @RequestParam String keyword,
            @Parameter(description = "페이지 번호", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "한 페이지에 보여줄 게시물 개수", example = "12") @RequestParam(defaultValue = "12") int size) {

        return ResponseEntity.ok(ApiSuccessResponse.success(boardEsService.searchBoard(REVIEW_CATEGORY_ID, keyword, page, size), "입양 후기 게시판 검색 성공"));
    }

    @Operation(summary = "sns 홍보 게시글 검색")
    @GetMapping("/sns/elasticsearch")
    public ResponseEntity<ApiResponse<Page<BoardListDTO>>> searchSnsBoard(
            @Parameter(description = "검색어", example = "제목") @RequestParam String keyword,
            @Parameter(description = "페이지 번호", example = "0")@RequestParam(defaultValue = "0") int page,
            @Parameter(description = "한 페이지에 보여줄 게시물 개수", example = "12") @RequestParam(defaultValue = "12") int size) {

        return ResponseEntity.ok(ApiSuccessResponse.success(boardEsService.searchBoard(SNS_CATEGORY_ID, keyword, page, size), "sns 홍보 게시판 검색 성공"));
    }

}