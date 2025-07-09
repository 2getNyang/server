package com.project.nyang.modules.board.sns.service;

import com.project.nyang.modules.board.Board;
import com.project.nyang.modules.board.sns.dto.SNSBoardDTO;
import com.project.nyang.modules.board.sns.repository.SNSBoardRepository;
import com.project.nyang.modules.board.sns.repository.SNSBoardRepositoryCustom;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.reference.entity.Category;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SNSBoardService {

    private final SNSBoardRepository snsBoardRepository;
    private final SNSBoardRepositoryCustom snsBoardRepositoryCustom;
    private final EntityManager em;

    /**
     * SNS 게시글 등록
     */
    @Transactional
    public Long createBoard(SNSBoardDTO dto, Long userId) {
        User user = em.getReference(User.class, userId);
        Category category = em.getReference(Category.class, 4L); // SNS 게시판 카테고리 4

        Board board = Board.builder()
                .user(user)
                .category(category)
                .boardTitle(dto.getTitle())
                .boardContent(dto.getContent())
                .instagramLink(dto.getInstagramLink())
                .build();

        Board saved = snsBoardRepository.save(board);
        return saved.getId();
    }

    /**
     * SNS 게시글 상세 조회
     */
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

    /* 페이징 전체 목록*/
    @Transactional(readOnly = true)
    public Page<SNSBoardDTO> getBoardsPaged(Pageable pageable) {
        Page<Board> boardsPage = snsBoardRepository.findByCategory_CategoryId(4L, pageable);

        List<SNSBoardDTO> dtoList = boardsPage.getContent()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, boardsPage.getTotalElements());
    }

    /* 페이징 검색 목록 */

    /* SNS 게시글 수정 */
    @Transactional
    public void updateBoard(Long boardId, SNSBoardDTO dto, Long userId) {
        Board board = snsBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + boardId));

        if (!board.getCategory().getCategoryId().equals(4L)) {
            throw new IllegalArgumentException("SNS 게시글만 수정 가능합니다. ID: " + boardId);
        }

        if (!board.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        snsBoardRepositoryCustom.updateSNSBoard(boardId, dto.getTitle(), dto.getContent(), dto.getInstagramLink());

    }


    /* SNS 게시글 삭제 */
    @Transactional
    public void deleteBoard(Long boardId, Long userId) {
        Board board = snsBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + boardId));

        if (!board.getCategory().getCategoryId().equals(4L)) {
            throw new IllegalArgumentException("SNS 게시글만 삭제 가능합니다. ID: " + boardId);
        }

        if (!board.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        snsBoardRepository.delete(board);
    }

    /**
     * Entity -> DTO 변환
     */
    private SNSBoardDTO toDTO(Board board) {
        SNSBoardDTO dto = new SNSBoardDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getBoardTitle());
        dto.setContent(board.getBoardContent());
        dto.setInstagramLink(board.getInstagramLink());
        dto.setViewCount(board.getViewCount());
//        dto.setUserId(board.getUser().getId());

        return dto;
    }
}
