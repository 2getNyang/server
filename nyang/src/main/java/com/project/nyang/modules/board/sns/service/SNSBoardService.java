package com.project.nyang.modules.board.sns.service;


import com.project.nyang.global.exception.CustomException;
import com.project.nyang.modules.board.Board;
import com.project.nyang.modules.board.sns.dto.SNSBoardDTO;
import com.project.nyang.modules.board.sns.repository.SNSBoardRepository;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.reference.entity.Category;
import com.project.nyang.reference.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;



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

    /* 카테고리 타입: sns게시판
    * 카테고리id 1:동물공고 / 2:입양후기 3:sns홍보 4:실종/목격제보 */
    private static final Long SNS_CATEGORY_ID = 3L;

    /* SNS 게시판 글 등록 */
    @Transactional
    public SNSBoardDTO createSNSBoard(SNSBoardDTO boardDTO){

        if(boardDTO.getUser_id() == null){
            throw new IllegalArgumentException("유저못찾아잉");
        }
        Category category = categoryRepository.findById(SNS_CATEGORY_ID)
                .orElseThrow(() -> new IllegalArgumentException("SNS 카테고리를 찾을 수 없습니다."));


        // User 조회
       /* User user = userRepository.findById(boardDTO.getUser_id())
                .orElseThrow(() -> new CustomException(UNAUTHORIZED));*/

        Board board = Board.builder()
                .category(category)
                .boardTitle(boardDTO.getBoardTitle())
                .boardContent(boardDTO.getBoardContent())
                .viewCount(0L)
                .instagramLink(boardDTO.getInstagramLink())
//                .user(user)
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

    /* SNS 게시판 글 수정 */
    @Transactional
    public void updateBoard(SNSBoardDTO dto, Long userId) {

        /* 유저인지 확인하는 코드 추가해야합니다 */

        Board board = snsBoardRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + dto.getId()));

        if (board.getCategory() == null ||
                !board.getCategory().getCategoryId().equals(SNS_CATEGORY_ID) ) {
            /* custom errorcode 로 써야하는데 에헷 >< 나중에 수정할게요 */
            throw new IllegalArgumentException("SNS 게시글만 수정 가능합니다. 게시글의 카테고리를 확인해주세요. 호출된 게시글의 ID: " + dto.getId());
        }else if(board.getDeletedAt() != null){
            throw new IllegalArgumentException("해당 글은 삭제되었습니다. ID : " + dto.getId());
        }

        if (!board.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        if(board.getInstagramLink() == null){
            throw new IllegalArgumentException("SNS 게시글 링크가 사라졌습니다.");
        }

        Board updatedBoard = board.toBuilder()
                .boardTitle(dto.getBoardTitle() != null ? dto.getBoardTitle() : board.getBoardTitle())
                .boardContent(dto.getBoardContent() != null ? dto.getBoardContent() : board.getBoardContent())
                .instagramLink(dto.getInstagramLink() != null ? dto.getInstagramLink() : board.getInstagramLink())
                .user(board.getUser())
                .viewCount(dto.getViewCount())
                .build();

        snsBoardRepository.save(updatedBoard); // save로 반영
    }

    /* SNS 게시판 글 삭제 */
    @Transactional
    public void deleteBoard(SNSBoardDTO dto, Long userId) {
        Board board = snsBoardRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + dto.getId()));

        if (board.getCategory() == null ||
                !board.getCategory().getCategoryId().equals(SNS_CATEGORY_ID)) {
            throw new IllegalArgumentException("SNS 게시글만 삭제 가능합니다. 현재 카테고리 id: " + board.getCategory().getCategoryId());
        }

        if (board.getDeletedAt() != null) {
            throw new IllegalArgumentException("이미 삭제된 게시글입니다. ID: " + dto.getId());
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
