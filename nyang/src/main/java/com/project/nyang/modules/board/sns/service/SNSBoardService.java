package com.project.nyang.modules.board.sns.service;

import com.project.nyang.modules.board.Board;
import com.project.nyang.modules.board.sns.dto.SNSBoardDTO;
import com.project.nyang.modules.board.sns.repository.SNSBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * sns board service
 *
 * @author : 이은서
 * @fileName : SNSBoardService
 * @since : 25. 7. 9.
 */
@Service
@RequiredArgsConstructor
public class SNSBoardService {
    private final SNSBoardRepository snsBoardRepository;

    /* board 게시글 상세 조회 */
    @Transactional
    public SNSBoardDTO getBoardDetail(Long boardId) {
        Board board = snsBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + boardId));

        if (board.getCategory() == null || !board.getCategory().getCategoryId().equals(4L)) {
            throw new IllegalArgumentException("SNS 게시글만 조회 가능합니다. ID: " + boardId);
        }

        board.increaseViewCount();
        return toDTO(board);
    }


    private SNSBoardDTO toDTO(Board board) {
        SNSBoardDTO dto = new SNSBoardDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getBoardTitle());
        dto.setContent(board.getBoardContent());
        dto.setInstagramLink(board.getInstagramLink());
        dto.setViewCount(board.getViewCount());
        return dto;
    }
}
