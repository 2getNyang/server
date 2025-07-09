package com.project.nyang.modules.board.sns.service;

import com.project.nyang.modules.board.Board;
import com.project.nyang.modules.board.sns.dto.SNSBoardDTO;
import com.project.nyang.modules.board.sns.repository.SNSBoardRepository;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.reference.entity.Category;
import com.project.nyang.reference.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SNSBoardService {

    private final SNSBoardRepository snsBoardRepository;
    /* */
    private static final Long SNS_CATEGORY_ID = 4L;

    /* SNS 게시판 글 등록 */


    /* SNS 게시판 글 상세조회 */
    @Transactional
    public SNSBoardDTO getBoardDetail(Long boardId) {
        Board board = snsBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + boardId));

        if (!isSNSBoard(board)) {
            throw new IllegalArgumentException("SNS 게시글만 조회 가능합니다. ID: " + boardId);
        }

        board.increaseViewCount();

        return entityToDto(board);
    }

    /* SNS 게시판 페이징 */
    @Transactional(readOnly = true)
    public Page<SNSBoardDTO> getBoardsPaged(Pageable pageable) {
        Page<Board> boardsPage = snsBoardRepository.findByCategory_CategoryId(4L, pageable);

        List<SNSBoardDTO> dtoList = boardsPage
                .map(this::entityToDto)
                .getContent();

        return new PageImpl<>(dtoList, pageable, boardsPage.getTotalElements());
    }

    /* SNS 게시판 글 수정 */
    @Transactional
    public void updateBoard(Long boardId, SNSBoardDTO dto, Long userId) {
        Board board = snsBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + boardId));

        if (!isSNSBoard(board)) {
            throw new IllegalArgumentException("SNS 게시글만 수정 가능합니다. ID: " + boardId);
        }

        if (!board.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        // Dirty Checking 으로 자동 반영되므로 save 불필요
    }

    /* SNS 게시판 글 삭제 */
    @Transactional
    public void deleteBoard(Long boardId, Long userId) {
        Board board = snsBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + boardId));

        if (!isSNSBoard(board)) {
            throw new IllegalArgumentException("SNS 게시글만 삭제 가능합니다. ID: " + boardId);
        }

        if (!board.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        snsBoardRepository.delete(board);
    }

    /* SNS 게시판 카테고리 확인 */
    private boolean isSNSBoard(Board board) {
        return board.getCategory() != null && board.getCategory().getCategoryId().equals(4L);
    }

    /* Entity -> DTO 변환 */
    private SNSBoardDTO entityToDto(Board board) {
        return SNSBoardDTO.builder()
                .id(board.getId())
                .title(board.getBoardTitle())
                .content(board.getBoardContent())
                .instagramLink(board.getInstagramLink())
                .viewCount(board.getViewCount())
                .build();
    }

}
