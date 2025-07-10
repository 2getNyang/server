package com.project.nyang.modules.board.sns.controller;

import com.project.nyang.global.common.api.ApiResponse;
import com.project.nyang.global.common.api.ApiSuccessResponse;
import com.project.nyang.modules.board.sns.dto.SNSBoardDTO;
import com.project.nyang.modules.board.sns.service.SNSBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /* SNS 게시판 글 등록 */
    @PostMapping
    public ResponseEntity<ApiResponse<SNSBoardDTO>> createSNSBoard(
            @PathVariable String slug,
            @RequestBody SNSBoardDTO boardDTO
    ) {
        validateSlug(slug);
        SNSBoardDTO createdBoard = snsBoardService.createSNSBoard(boardDTO);
        return ResponseEntity.ok(ApiSuccessResponse.success(createdBoard));
    }


    /* SNS 게시판 글 수정 */
    @PutMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> updateSNSBoard(
            @PathVariable String slug,
            @PathVariable Long boardId,
            @RequestBody SNSBoardDTO boardDTO
    ) {
        System.out.println("Board ID : "+boardId);
        System.out.println("Board DTO : "+boardDTO);
        validateSlug(slug);
        snsBoardService.updateSNSBoard(boardId,boardDTO );
        return ResponseEntity.ok(ApiSuccessResponse.success(null));

    }

    /* SNS 게시판 글 삭제
    * 삭제는 나중에 Auth 넣고 jwtToken 으로 invalid User 인지 확인하고
    * 지우는거로 변경해주세요  */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> deleteSNSBoard(
            @PathVariable String slug,
            @PathVariable Long boardId,
            @RequestParam Long userId
    ) {
        validateSlug(slug);
        snsBoardService.deleteSNSBoard(boardId,userId);
        return ResponseEntity.ok(ApiSuccessResponse.success(null));
    }

}