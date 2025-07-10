package com.project.nyang.modules.board.sns.service;


import com.project.nyang.global.exception.CustomException;
import com.project.nyang.modules.board.Board;
import com.project.nyang.modules.board.sns.dto.SNSBoardDTO;
import com.project.nyang.modules.board.sns.repository.SNSBoardRepository;
import com.project.nyang.modules.board.sns.repository.UserRepository; //개발을 위한 user repository 나중엔 user.UserRepository 로 변경해주세요
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.reference.entity.Category;
import com.project.nyang.reference.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.project.nyang.global.exception.ErrorCode.UNAUTHORIZED;


/**
 * snsboard service 입니다
 *
 * @author : 이은서
 * @fileName : SNSBoardService
 * @since : 25. 7. 8.
 */
@Service
@RequiredArgsConstructor
public class SNSBoardService {
    private final SNSBoardRepository snsBoardRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    /* 카테고리 타입: sns게시판
    * 카테고리id 1:동물공고 / 2:입양후기 3:sns홍보 4:실종/목격제보 */
    private static final Long SNS_CATEGORY_ID = 3L;

    /* SNS 게시판 글 등록 */
    @Transactional
    public SNSBoardDTO createSNSBoard(SNSBoardDTO boardDTO){

        if(boardDTO.getUserId() == null){
            throw new IllegalArgumentException("유저못찾아잉");
        }
        Category category = categoryRepository.findById(SNS_CATEGORY_ID)
                .orElseThrow(() -> new IllegalArgumentException("SNS 카테고리를 찾을 수 없습니다."));


        // User 조회
        User user = userRepository.findById(boardDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. id=" + boardDTO.getUserId()));


        Board board = Board.builder()
                .category(category)
                .boardTitle(boardDTO.getBoardTitle())
                .boardContent(boardDTO.getBoardContent())
                .viewCount(0L)
                .instagramLink(boardDTO.getInstagramLink())
                .user(user)
                .build();
        snsBoardRepository.save(board);
        return toDto(board);

    }


    /* SNS 게시판 글 상세조회 */
    @Transactional(readOnly = true)
    public SNSBoardDTO getBoardDetail(Long boardId) {
        Board board = snsBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + boardId));

        if (!board.getCategory().getCategoryId().equals(SNS_CATEGORY_ID)) {
            throw new IllegalArgumentException("SNS 게시글만 조회 가능합니다. 현재 불러오는 카테고리 id " + boardId);
        }

        board.increaseViewCount();

        return toDto(board);
    }
    /* SNS 게시글 수정*/
    @Transactional
    public void updateSNSBoard(Long boardId, SNSBoardDTO dto) {

        // 게시글 작성자가 맞는지
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다. ID: " + dto.getUserId()));

        // 경로 게시글 ID 와 본문 게시글 ID 가 다를때
        if (dto.getId() != null && !dto.getId().equals(boardId)) {
            throw new IllegalArgumentException("요청 경로의 게시글 ID와 요청 본문의 게시글 ID가 일치하지 않습니다.");
        }


        Board board = snsBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + boardId));

        if (board.getCategory() == null ||
                !board.getCategory().getCategoryId().equals(SNS_CATEGORY_ID)) {
            throw new IllegalArgumentException("SNS 게시글만 수정 가능합니다. 호출된 게시글 ID: " + boardId);
        } else if (board.getDeletedAt() != null) {
            throw new IllegalArgumentException("해당 글은 삭제되었습니다. ID: " + boardId);
        }

        if (!board.getUser().getId().equals(dto.getUserId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        if (board.getInstagramLink() == null) {
            throw new IllegalArgumentException("SNS 게시글 링크가 사라졌습니다.");
        }

        Board updatedBoard = board.toBuilder()
                .boardTitle(dto.getBoardTitle() != null ? dto.getBoardTitle() : board.getBoardTitle())
                .boardContent(dto.getBoardContent() != null ? dto.getBoardContent() : board.getBoardContent())
                .instagramLink(dto.getInstagramLink() != null ? dto.getInstagramLink() : board.getInstagramLink())
                .user(board.getUser())
                .viewCount(dto.getViewCount() != null ? dto.getViewCount() : board.getViewCount())
                .build();

        snsBoardRepository.save(updatedBoard);
    }


    /* SNS 게시판 글 삭제
    * 삭제는 나중에 user 를 Request Param 으로 넣지말고
    * JwtToken 으로 Invalid User 인지 확인하는 과정을 넣읍시다.*/
    @Transactional
    public void deleteSNSBoard(Long boardId,Long userId) {
        Board board = snsBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + boardId));

        if (board.getCategory() == null ||
                !board.getCategory().getCategoryId().equals(SNS_CATEGORY_ID)) {
            throw new IllegalArgumentException("SNS 게시글만 삭제 가능합니다. 현재 카테고리 id: " + board.getCategory().getCategoryId());
        }

        if (board.getDeletedAt() != null) {
            throw new IllegalArgumentException("이미 삭제된 게시글입니다. ID: " + boardId);
        }

        if (!board.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다. 작성자 ID: " + board.getUser().getId());
        }

        board.markDeleted();

        snsBoardRepository.save(board);
    }


    /* SNS 게시판 페이징 */
    @Transactional(readOnly = true)
    public Page<SNSBoardDTO> getBoardsPaged(Pageable pageable) {
        // category ID 가 4인 엔티티만 페이징 조회
        Page<Board> boardsPage = snsBoardRepository.findByCategory_CategoryId(SNS_CATEGORY_ID, pageable);

        List<SNSBoardDTO> dtoList = boardsPage
                .map(this::toDto)
                .getContent();

        return new PageImpl<>(dtoList, pageable, boardsPage.getTotalElements());
    }
    /* SNS 게시판 검색 페이징
     * @param keyword 검색 키워드 (제목 또는 내용)
     * @param pageable 페이징 정보
     * @return SNSBoardDTO 페이징 결과
     */
    @Transactional(readOnly = true)
    public Page<SNSBoardDTO> searchSNSBoards(String keyword, Pageable pageable) {
        Page<Board> boardsPage = snsBoardRepository
                .findByCategory_CategoryIdAndBoardTitleContainingOrCategory_CategoryIdAndBoardContentContaining(
                        SNS_CATEGORY_ID, keyword,
                        SNS_CATEGORY_ID, keyword,
                        pageable
                );

        List<SNSBoardDTO> dtoList = boardsPage
                .map(this::toDto)
                .getContent();

        return new PageImpl<>(dtoList, pageable, boardsPage.getTotalElements());
    }

    /* Entity -> DTO 변환 */
    private SNSBoardDTO toDto(Board board) {
        return SNSBoardDTO.builder()
                .id(board.getId())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .instagramLink(board.getInstagramLink())
                .viewCount(board.getViewCount())
                .build();
    }


}
