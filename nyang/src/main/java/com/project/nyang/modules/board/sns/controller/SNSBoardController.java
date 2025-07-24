package com.project.nyang.modules.board.sns.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.global.security.core.CustomUserDetails;
import com.project.nyang.modules.board.sns.dto.SNSBoardDTO;
import com.project.nyang.modules.board.sns.dto.SNSBoardUpdateDTO;
import com.project.nyang.modules.board.sns.dto.SNSBoardUpdateFormDTO;
import com.project.nyang.modules.board.sns.service.SNSBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Tag(name = "🐥SNS Board API", description = "내새꾸 자랑하는 SNS 게시판 관련 기능")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/boards/{slug}")
public class SNSBoardController {

    private final SNSBoardService snsBoardService;

    private void validateSlug(String slug) {
        if (!"sns".equals(slug)) {
            throw new IllegalArgumentException("category 가 sns 맞는지 확인해주세요 " + slug);
        }
    }

    @Operation(summary = "SNS 게시글 검색", description = "SNS 게시글 검색")
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

    /*
    * sns 게시글 전체목록 조회 (paging)
    * 여기도 엘라스틱 서치 넣어야해요
    */
    @Operation(summary = "SNS 게시글 전체 페이징", description = "SNS 카테고리 전체 게시글 목록 조회 (페이징)")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<SNSBoardDTO>>> getBoardList(
            @PathVariable String slug,
            Pageable pageable
    ) {
        validateSlug(slug);
        Page<SNSBoardDTO> result = snsBoardService.getBoardsPaged(pageable);
        return ResponseEntity.ok(ApiSuccessResponse.success(result));
    }



    /* SNS 게시판 상세조회 */
    @Operation(summary = "SNS 게시글 상세조회", description = "게시글 ID로 상세 조회")
    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<SNSBoardDTO>> getBoardDetail(
            @PathVariable String slug,
            @PathVariable Long boardId
    ) {
        validateSlug(slug);
        SNSBoardDTO dto = snsBoardService.getBoardDetail(boardId);
        return ResponseEntity.ok(ApiSuccessResponse.success(dto));
    }


    /* SNS 게시판 글 등록
    * 요부분은 순주님 설명을 봐도 모르겠어서 지선생~ 도와줘~*/
    @Operation(summary = "SNS 게시글 등록", description = "게시글 + 이미지 등록")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "등록 성공")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<SNSBoardDTO>> createSNSBoard(
            @PathVariable String slug,
            @RequestPart("dto") String dtoJson, // 🔥 JSON 문자열로 받음
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        validateSlug(slug);
        Long userId = customUserDetails.getId();

        // 수동 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        SNSBoardDTO boardDTO;
        try {
            boardDTO = objectMapper.readValue(dtoJson, SNSBoardDTO.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON 파싱 실패", e);
        }

        SNSBoardDTO createdBoard = snsBoardService.createSNSBoard(boardDTO, imageFiles, userId);
        return ResponseEntity.ok(ApiSuccessResponse.success(createdBoard));
    }

    /* 글 수정폼 호출 */
    @Operation(summary = "SNS 게시글 수정폼 호출")
    @GetMapping("/{boardId}/form")
    public ResponseEntity<ApiResponse<SNSBoardUpdateFormDTO>> getSNSBoardFormEdit(
            @Parameter(description = "수정할 게시글의 ID", example = "13")
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        Long userId = customUserDetails.getId();
        SNSBoardUpdateFormDTO formDTO = snsBoardService.getSNSBoardUpdateForm(boardId, userId);
        return ResponseEntity.ok(ApiSuccessResponse.success(formDTO,"SNS 게시글 수정 폼 호출 완료"));
    }

    // 수정
    @Operation(summary = "SNS 게시글 수정", description = "SNS 게시판 게시글 수정 (페이징)")
    @PutMapping(
        value = "/{boardId}",
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<ApiResponse<Void>> updateSNSBoard(
            @PathVariable String slug,
            @PathVariable Long boardId,
            @RequestPart("dto") SNSBoardUpdateDTO updateDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> newImages,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        validateSlug(slug);
        Long userId = customUserDetails.getId();
        snsBoardService.updateSNSBoard(boardId, updateDTO, userId, newImages);
        return ResponseEntity.ok(ApiSuccessResponse.success(null));
    }


    // 삭제
    @Operation(summary = "SNS 게시글 삭제", description = "SNS 게시글 삭제")
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