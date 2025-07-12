package com.project.nyang.modules.board.reveiw.service;

import com.project.nyang.modules.adoption.entity.PetApplicationForm;
import com.project.nyang.modules.board.reveiw.repository.AdoptionRepository;
import com.project.nyang.modules.board.Board;
import com.project.nyang.modules.board.reveiw.dto.ReveiwBoardDetailDTO;
import com.project.nyang.modules.board.reveiw.dto.ReveiwBoardListDTO;
import com.project.nyang.modules.board.reveiw.dto.ReviewBoardCreateDTO;
import com.project.nyang.modules.board.reveiw.repository.ReviewBoardRepository;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import com.project.nyang.reference.entity.Category;
import com.project.nyang.reference.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    private final UserRepository userRepository;
    private final AdoptionRepository adoptionRepository;
    private static final Long CATEGORY_ID = 2L;

    /**
     * 입양 후기 게시글 등록
     *
     * @param boardDTO
     * @param userId:  사용자 ID
     */
    @Transactional
    public void createReviewBoard(ReviewBoardCreateDTO boardDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 올바르지 않습니다."));

        Category category = categoryRepository.findById(CATEGORY_ID).orElseThrow(() -> new IllegalArgumentException("카테고리 번호가 잘못되었습니다 :" + CATEGORY_ID));

        PetApplicationForm form = null;
        if(boardDTO.getFormId() != null){
            form = adoptionRepository.findById(boardDTO.getFormId()).orElseThrow(() -> new IllegalArgumentException("입양 신청 내역이 확인되지 않습니다."));
        }

        /** mysql 저장 **/
        Board board = Board.builder()
                .boardTitle(boardDTO.getBoardTitle())
                .boardContent(boardDTO.getBoardContent())
                .category(category)
                .user(user)
                .petApplicationForm(form)
                .build();

        // TODO. image 추가 필요

        reviewBoardRepository.save(board);

    }

    /**
     * 입양 후기 게시글 리스트 조회
     *
     * @param page: 현재 조회해올 페이지
     * @param size: 한 페이지에 보여줄 게시물 개수
     * @return
     */
    @Transactional
    public Page<ReveiwBoardListDTO> getReviewBoards(int page, int size) {
        Page<Board> boards = reviewBoardRepository.findAllByDeletedAtIsNull(PageRequest.of(page, size));

        return boards.map(board -> ReveiwBoardListDTO.builder()
                .nickname(board.getUser().getNickname())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .imageUrl(
                        board.getImages() != null && !board.getImages().isEmpty()
                                ? board.getImages().get(0).getS3Url()
                                : null
                )
                .createdAt(board.getCreatedAt())
                .boardViewCount(board.getViewCount())
                .likeItCount(board.getLikeList().size())
                .isLiked(board.existsLikeBy(board.getId(), board.getUser().getId()))
                .build());
    }

    /**
     * 입양 후기 게시글 상세 조회
     *
     * @param id: 게시글 ID
     * @return
     */
    @Transactional
    public ReveiwBoardDetailDTO getReviewBoardDetail(Long id) {
        Board board = reviewBoardRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다: " + id));

        // 1. 조회수 증가
        board.increaseViewCount();

        List<ReveiwBoardDetailDTO.ImageDTO> imageList = board.getImages().stream()
                .map(ReveiwBoardDetailDTO.ImageDTO::toDTO)
                .collect(Collectors.toList());

        List<ReveiwBoardDetailDTO.CommentDTO> commentDTOList = board.getComments().stream()
                .filter(comment -> comment.getDeletedAt() == null)
                .map(ReveiwBoardDetailDTO.CommentDTO::toDTO)
                .collect(Collectors.toList());

        PetApplicationForm form = null;
        if (board.getPetApplicationForm() != null) {
            form = adoptionRepository.findById(board.getPetApplicationForm().getId()).orElseThrow(() -> new IllegalArgumentException("입양 신청 내역이 없습니다."));
        }

        // 2. 입양 후기 게시글 상세 정보 담기
        ReveiwBoardDetailDTO boardDto = ReveiwBoardDetailDTO.builder()
                .nickname(board.getUser().getNickname())
                .userId(board.getUser().getId())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .images(imageList)
                .comments(commentDTOList)
                .createdAt(board.getCreatedAt())
                .boardViewCount(board.getViewCount())
                .likeItCount(board.getLikeList().size())
                .isLiked(board.existsLikeBy(board.getId(), board.getUser().getId()))
                .petApplicationDTO(form != null ? ReveiwBoardDetailDTO.PetApplicationDTO.toDTO(form) : null)
                .build();

        return boardDto;
    }

    /**
     * 입양 후기 게시글 삭제
     *
     * @param id: 게시글 ID
     * @param userId: 사용자 ID
     */
    @Transactional
    public void deleteReviewBoard(Long id, Long userId) {
        Board board = reviewBoardRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다: " + id));

        if(!board.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        board.markDeleted();

        // comments, likeList, images는 cascade 속성으로 함께 사라짐
    }

    /**
     * 입양 후기 게시글 수정
     *
     * @param id:      게시글 ID
     * @param boardDTO
     * @param userId:  사용자 ID
     */
    @Transactional
    public void updateReviewBoard(Long id, Long userId, ReviewBoardCreateDTO boardDTO) {

        Board board = reviewBoardRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다: " + id));

        if(!board.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        PetApplicationForm form = null;
        if(boardDTO.getFormId() != null){
            form = adoptionRepository.findById(boardDTO.getFormId()).orElseThrow(() ->new IllegalArgumentException("입양 신청 내역이 없습니다."));
        }

        board.updateReviewBoard(boardDTO, form);

        // TODO. image 추가 필요

    }
}