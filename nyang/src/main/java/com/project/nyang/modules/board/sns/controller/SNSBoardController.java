package com.project.nyang.modules.board.sns.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.global.security.core.CustomUserDetails;
import com.project.nyang.modules.board.sns.dto.SNSBoardDTO;
import com.project.nyang.modules.board.sns.service.SNSBoardService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

/**
 *
 * sns board controller 
 * @fileName        : SNSBoardController
 * @author          : 이은서
 * @since           : 25. 7. 9.
 * 
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/{slug}")
public class SNSBoardController {

    private final SNSBoardService snsBoardService;
    private void validateSlug(String slug) {
        if (!"sns".equals(slug)) {
            throw new IllegalArgumentException("category 가 sns 맞는지 확인해주세요 " + slug);
        }
    }

    /* SNS 게시판 검색 */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<SNSBoardDTO>>> searchSNSBoards(
            @PathVariable String slug,
            @RequestParam String keyword,
            Pageable pageable
    ) {
        validateSlug(slug);
        Page<SNSBoardDTO> results = snsBoardService.searchSNSBoards(keyword, pageable);
        return ResponseEntity.ok(ApiSuccessResponse.success(results));
    }

    /* SNS 게시판 글 등록
    * 요부분은 순주님 설명을 봐도 모르겠어서 지선생~ 도와줘~*/
    @Operation(summary = "SNS 게시글 등록", description = "게시글 + 이미지 등록")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "등록 성공")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<SNSBoardDTO>> createSNSBoard(
            @PathVariable String slug,
            @RequestPart("dto") SNSBoardDTO boardDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        validateSlug(slug);
        Long userId = customUserDetails.getId();
        SNSBoardDTO createdBoard = snsBoardService.createSNSBoard(boardDTO, imageFiles, userId);
        return ResponseEntity.ok(ApiSuccessResponse.success(createdBoard));
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> updateSNSBoard(
            @PathVariable String slug,
            @PathVariable Long boardId,
            @RequestBody SNSBoardDTO boardDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        validateSlug(slug);
        Long userId = customUserDetails.getId();
        snsBoardService.updateSNSBoard(boardId, boardDTO, userId);
        return ResponseEntity.ok(ApiSuccessResponse.success(null));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> deleteSNSBoard(
            @PathVariable String slug,
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        validateSlug(slug);
        Long userId = customUserDetails.getId();
        snsBoardService.deleteSNSBoard(boardId, userId);
        return ResponseEntity.ok(ApiSuccessResponse.success(null));
    }


}