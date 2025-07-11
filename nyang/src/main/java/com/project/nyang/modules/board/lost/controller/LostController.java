package com.project.nyang.modules.board.lost.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.modules.board.lost.dto.LostCreateRequestDto;
import com.project.nyang.modules.board.lost.dto.LostDetailResponseDTO;
import com.project.nyang.modules.board.lost.dto.LostListResponseDTO;
import com.project.nyang.modules.board.lost.service.LostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.List;

/**
 * 실종/목격 게시판 컨트롤러입니다.
 *
 * @author : 선순주
 * @fileName : LostController
 * @since : 2025-07-09
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/lost")
public class LostController {

    private final LostService lostService;
    private final Long categoryId =  4L;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LostListResponseDTO>>> getBoardsByCategory(
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<LostListResponseDTO> boards = lostService.getLostBoard(categoryId, pageable);
        return ResponseEntity.ok(ApiSuccessResponse.success(boards));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<LostDetailResponseDTO>> getBoardDetail(@PathVariable Long boardId) {
        LostDetailResponseDTO responseDTO = lostService.getLostDetail(boardId);
        return ResponseEntity.ok(ApiSuccessResponse.success(responseDTO));
    }

    //게시글 저장, 이미지 s3이미지 업로드 및 이미지 저장
    @PostMapping (consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiResponse<Long>> createLostBoard(
            @RequestPart("dto") LostCreateRequestDto dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {

        Long boardId = lostService.createLostBoard(dto, images);
        return ResponseEntity.ok(ApiSuccessResponse.success(boardId));
    }

    //게시글 삭제(Board 테이블, Image 테이블, s3이미지 삭제)
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Long>> deleteBoard(@PathVariable Long boardId){
        lostService.deleteLostBoard(boardId);
        return ResponseEntity.ok(ApiSuccessResponse.success(boardId, "게시글이 삭제되었습니다."));
    }
}