package com.project.nyang.modules.board.review.service;

import com.project.nyang.global.common.S3.S3Service;
import com.project.nyang.global.common.entity.BaseTime;
import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.modules.adoption.entity.PetApplicationForm;
import com.project.nyang.global.elasticsearch.board.dto.BoardEsDocument;
import com.project.nyang.global.elasticsearch.board.repository.BoardEsRepository;
import com.project.nyang.modules.board.review.dto.ReviewBoardUpdateDTO;
import com.project.nyang.modules.board.review.repository.AdoptionRepository;
import com.project.nyang.modules.board.entity.Board;
import com.project.nyang.modules.board.review.dto.ReveiwBoardDetailDTO;
import com.project.nyang.modules.board.review.dto.ReveiwBoardListDTO;
import com.project.nyang.modules.board.review.dto.ReviewBoardCreateDTO;
import com.project.nyang.modules.board.review.repository.ReviewBoardRepository;
import com.project.nyang.modules.image.entity.Image;
import com.project.nyang.modules.like.repository.LikeRepository;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import com.project.nyang.reference.entity.Category;
import com.project.nyang.reference.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final BoardEsRepository boardEsRepository;
    private final S3Service s3Service;
    private static final Long CATEGORY_ID = 2L;
    private final LikeRepository likeRepository;

    /**
     * 입양 후기 게시글 등록
     *
     * @param boardDTO
     * @param userId:  사용자 ID
     */
    @Transactional
    public void createReviewBoard(Long userId, ReviewBoardCreateDTO boardDTO, List<MultipartFile> images) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED)
);

        Category category = categoryRepository.findById(CATEGORY_ID).orElseThrow(() -> new IllegalArgumentException("카테고리 번호가 잘못되었습니다 :" + CATEGORY_ID));

        PetApplicationForm form = null;
        if(boardDTO.getFormId() != null){
            form = adoptionRepository.findById(boardDTO.getFormId()).orElseThrow(() -> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));
        }

        /** mysql 저장 **/
        Board board = Board.builder()
                .boardTitle(boardDTO.getBoardTitle())
                .boardContent(boardDTO.getBoardContent())
                .category(category)
                .user(user)
                .petApplicationForm(form)
                .build();

        // 이미지 업로드 및 엔티티 생성
        if (images != null && !images.isEmpty()) {
            if(images.size() >= 5) throw new IllegalArgumentException("이미지는 최대 5개까지 첨부가 가능합니다.");

            List<String> s3Urls = s3Service.uploadFile(images); //전체 URL 리스트 반환

            for (int i = 0; i < s3Urls.size(); i++) {
                MultipartFile file = images.get(i);
                String s3Url = s3Urls.get(i);
                String fileName = file.getOriginalFilename();
                String fileSize = String.valueOf(file.getSize());

                Image image = Image.builder()
                        .originFileName(fileName)
                        .s3Url(s3Url)
                        .fileSize(fileSize)
                        .thumbnailIs(i == 0 ? "Y" : "N")    //첫번쨰 이미지를 썸네일로
                        .board(board)
                        .build();

                board.getImages().add(image);
            }
        }
        reviewBoardRepository.save(board);

        /** elasticSearch 저장 */
        BoardEsDocument doc = BoardEsDocument.builder()
                .id(String.valueOf(board.getId()))
                .viewCount(board.getViewCount())
                .categoryId(board.getCategory().getCategoryId())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .createdAt(board.getCreatedAt().toString())
                .imageUrl(Optional.ofNullable(board.getImages())
                        .filter(boardImages -> !boardImages.isEmpty())
                        .map(boardImages -> boardImages.get(0).getS3Url())
                        .orElse(null))
                .nickname(board.getUser().getNickname())
                .build();

        boardEsRepository.save(doc);

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
        Page<Board> boards = reviewBoardRepository.findAllByDeletedAtIsNullAndCategory_CategoryId(PageRequest.of(page, size), CATEGORY_ID);

        return boards.map(board -> ReveiwBoardListDTO.builder()
                .id(board.getId())
                .nickname(board.getUser().getNickname())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .imageUrl(
                        board.getImages() != null && !board.getImages().isEmpty()
                                ? board.getImages().stream()
                                .filter(image -> image.getDeletedAt() == null && image.getThumbnailIs().equals("Y"))
                                .map(Image::getS3Url)
                                .findFirst()
                                .orElse(null)
                                : null
                )
                .createdAt(board.getCreatedAt())
                .boardViewCount(board.getViewCount())
                // 좋아요 여부, 좋아요 수 추후 view가 수정될 경우 활용
//                .likeItCount(board.getLikeList().size())
//                .isLiked(board.existsLikeBy(board.getId(), board.getUser().getId()))
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
        Board board = reviewBoardRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        // 1. 조회수 증가
        board.increaseViewCount();

        List<ReveiwBoardDetailDTO.ImageDTO> imageList = board.getImages().stream()
                .filter(image -> image.getDeletedAt() == null)
                .map(ReveiwBoardDetailDTO.ImageDTO::toDTO)
                .collect(Collectors.toList());

        List<ReveiwBoardDetailDTO.CommentDTO> commentDTOList = board.getComments().stream()
                .filter(comment -> comment.getDeletedAt() == null)
                .map(ReveiwBoardDetailDTO.CommentDTO::toDTO)
                .collect(Collectors.toList());

        PetApplicationForm form = null;
        if (board.getPetApplicationForm() != null) {
            form = adoptionRepository.findById(board.getPetApplicationForm().getId()).orElseThrow(() -> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));
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

        /** elasticSearch 조회수 증가 */
        BoardEsDocument doc = boardEsRepository.findById(String.valueOf(board.getId())).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        doc.increaseViewCount();
        boardEsRepository.save(doc);

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
        Board board = reviewBoardRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        if(!board.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        board.markDeleted();

        // comments, likeList, images -> delete
        board.getComments().forEach(BaseTime::markDeleted);
        likeRepository.deleteAll(board.getLikeList());
        board.getImages().forEach(Image::softDelete);

        /** elasticSearch 삭제 */
        boardEsRepository.deleteById(String.valueOf(board.getId()));

    }

    /**
     * 입양 후기 게시글 수정
     *
     * @param id:      게시글 ID
     * @param boardDTO
     * @param userId:  사용자 ID
     */
    @Transactional
    public void updateReviewBoard(Long userId, Long id, ReviewBoardUpdateDTO boardDTO, List<MultipartFile> newImages) {

        Board board = reviewBoardRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        if(!board.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN); // 403: 권한 없음
        }

        PetApplicationForm form = null;
        if(boardDTO.getFormId() != null){
            form = adoptionRepository.findById(boardDTO.getFormId()).orElseThrow(() ->new CustomException(ErrorCode.APPLICATION_NOT_FOUND));
        }

        board.updateReviewBoard(boardDTO, form);

        // 기존 이미지 중 삭제될 것들 soft delete
        List<Image> currentImages = board.getImages();
        List<Long> remainIds = boardDTO.getRemainImageIds();

        boolean thumbnailDeleted = false;

        for (Image image : currentImages) {
            if (!remainIds.contains(image.getImageId())) {
                if ("Y".equalsIgnoreCase(image.getThumbnailIs())) {
                    thumbnailDeleted = true;
                }
                image.softDelete();
            }
        }

        // 새 이미지 s3 업로드 및 Image 객체 생성
        List<Image> uploadedImages = new ArrayList<>();
        if (newImages != null && !newImages.isEmpty()) {
            List<String> uploadedUrls = s3Service.uploadFile(newImages);
            for (int i = 0; i < newImages.size(); i++) {
                MultipartFile file = newImages.get(i);
                String url = uploadedUrls.get(i);
                Image image = Image.builder()
                        .board(board)
                        .originFileName(file.getOriginalFilename())
                        .s3Url(url)
                        .fileSize(String.valueOf(file.getSize()))
                        .thumbnailIs("N")
                        .build();
                uploadedImages.add(image);
            }
            board.getImages().addAll(uploadedImages);
        }

        // 썸네일이 삭제되었을 경우 대체 썸네일 지정
        if (thumbnailDeleted) {
            Optional<Image> newThumbnail = currentImages.stream()
                    .filter(img -> img.getDeletedAt() == null)
                    .findFirst();

            if (newThumbnail.isEmpty()) {
                newThumbnail = uploadedImages.stream().findFirst();
            }

            newThumbnail.ifPresent(Image::markAsThumbnail);
        }

        BoardEsDocument doc = BoardEsDocument.builder()
                .id(String.valueOf(board.getId()))
                .viewCount(board.getViewCount())
                .categoryId(board.getCategory().getCategoryId())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .createdAt(board.getCreatedAt().toString())
                .imageUrl(board.getImages() != null && !board.getImages().isEmpty()
                        ? board.getImages().stream()
                        .filter(image -> image.getDeletedAt() == null && image.getThumbnailIs().equals("Y"))
                        .map(Image::getS3Url)
                        .findFirst()
                        .orElse(null)
                        : null)
                .nickname(board.getUser().getNickname())
                .build();

        boardEsRepository.save(doc);

    }
}