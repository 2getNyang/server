package com.project.nyang.modules.board.reveiw.service;

import com.project.nyang.modules.board.Board;
import com.project.nyang.modules.board.reveiw.dto.ReveiwBoardDetailDTO;
import com.project.nyang.modules.board.reveiw.dto.ReveiwBoardListDTO;
import com.project.nyang.modules.board.reveiw.dto.ReviewBoardDTO;
import com.project.nyang.modules.board.reveiw.repository.ReviewBoardRepository;
import com.project.nyang.reference.entity.Category;
import com.project.nyang.reference.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * 입양 후기 게시판 Service
 *
 * @author : 박세정
 * @fileName : ReveiwBoardService
 * @since : 2025-07-08
 */
@Service
@RequiredArgsConstructor
public class ReveiwBoardService {

    private final ReviewBoardRepository reviewBoardRepository;
    private final CategoryRepository categoryRepository;
    //    private final UserRepository userRepository;
    private static final Long CATEGORY_ID = 2L;


    @Transactional
    public void createReviewBoard(ReviewBoardDTO boardDTO) {
        // TODO. userId로 권한 확인 필요

        Category category = categoryRepository.findById(CATEGORY_ID).orElseThrow(() -> new RuntimeException("카테고리 번호가 잘못되었습니다 :" + CATEGORY_ID));

        // TODO. PetApplicationForm, User 추가
        /** mysql 저장 **/
        Board board = Board.builder().boardTitle(boardDTO.getBoardTitle()).boardContent(boardDTO.getBoardContent()).category(category)
//                .user()
//                .petApplicationForm()
                .build();

        // TODO. image 추가 필요

        reviewBoardRepository.save(board);

    }

    @Transactional
    public Page<ReveiwBoardListDTO> getReviewBoards(int page, int size) {
        Page<Board> boards = reviewBoardRepository.findAll(PageRequest.of(page, size));

        return boards.map(board -> ReveiwBoardListDTO.builder().nickname(board.getUser().getNickname()).boardTitle(board.getBoardTitle()).boardContent(board.getBoardContent()).imageUrl(board.getImages().get(0).getS3Url()).createdAt(board.getCreatedAt()).boardViewCount(board.getViewCount()).likeItCount(board.getLikeList().size()).isLiked(board.existsLikeBy(board.getId(), board.getUser().getId())).build());
    }

    @Transactional
    public ReveiwBoardDetailDTO getReviewBoardDetail(Long id) {
        Board board = reviewBoardRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다: " + id));

        // 1. 조회수 증가
        board.increaseViewCount();

        // 2. 입양 후기 게시글 상세 정보 담기
        ReveiwBoardDetailDTO boardDto = ReveiwBoardDetailDTO.builder().nickname(board.getUser().getNickname()).userId(board.getUser().getId()).boardTitle(board.getBoardTitle()).boardContent(board.getBoardContent()).images(board.getImages()).comments(board.getComments()).createdAt(board.getCreatedAt()).boardViewCount(board.getViewCount()).likeItCount(board.getLikeList().size()).isLiked(board.existsLikeBy(board.getId(), board.getUser().getId())).build();

        // TODO. 3. form_id가 있을 경우 입양 공고 조회해오기 -> 공고 담당자가 repository 완성했을 때 가져오면 될 거 같습니다.
        if (board.getPetApplicationForm().getId() != null) {
            boardDto.toBuilder()
                    // 해당 부분 수정 필요
                    .build();
        }
        return boardDto;
    }

    @Transactional
    public void deleteReviewBoard(Long id) {
        // TODO. userId로 권한 확인 필요
        Board board = reviewBoardRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다: " + id));

        board.markDeleted();

        // comments, likeList, images는 cascade 속성으로 함께 사라짐 
    }


    public void updateReviewBoard(Long id, ReviewBoardDTO boardDTO) {
        // TODO. userId로 권한 확인 필요
        Board board = reviewBoardRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다: " + id));

        // TODO. PetApplicationForm 추가
//        board.toBuilder()
//                .boardTitle(boardDTO.getBoardTitle())
//                .boardContent(boardDTO.getBoardContent())
//                //                .petApplicationForm()
//                .build();

        // TODO. image 추가 필요

    }

}