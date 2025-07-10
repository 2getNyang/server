package com.project.nyang.modules.board.sns.controller;

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
@RequestMapping("/api/v1/boards/sns")
public class SNSBoardController {

    private final SNSBoardService snsBoardService;

    /* SNS 게시판 검색 */
    @GetMapping("/search")
    public ResponseEntity<Page<SNSBoardDTO>> searchSNSBoards(
            @RequestParam String keyword,
            Pageable pageable
    ) {
        Page<SNSBoardDTO> results = snsBoardService.searchSNSBoards(keyword, pageable);
        return ResponseEntity.ok(results);
    }

    /* SNS 게시판 글 등록 */
    @PostMapping
    public ResponseEntity<SNSBoardDTO> createBoard(
            @PathVariable String slug,
            @RequestBody SNSBoardDTO boardDTO
    ) {
        SNSBoardDTO createdBoard = snsBoardService.createSNSBoard(boardDTO);
        return ResponseEntity.ok(createdBoard);
    }
}