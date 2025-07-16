package com.project.nyang.modules.board.sns.service;


import com.project.nyang.global.common.S3.S3Service;
import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.security.jwt.JwtTokenProvider;
import com.project.nyang.modules.board.entity.Board;
import com.project.nyang.modules.board.sns.dto.SNSBoardDTO;
import com.project.nyang.modules.board.sns.dto.SNSBoardUpdateDTO;
import com.project.nyang.modules.board.sns.repository.SNSBoardRepository;
import com.project.nyang.modules.image.entity.Image;
import com.project.nyang.modules.image.repository.ImageRepository;
import com.project.nyang.modules.like.repository.LikeRepository;
import com.project.nyang.modules.user.repository.UserRepository; //개발을 위한 user repository 나중엔 user.UserRepository 로 변경해주세요
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.reference.entity.Category;
import com.project.nyang.reference.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.nyang.global.exception.ErrorCode.*;


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
    private final ImageRepository imageRepository;
    private final LikeRepository likeRepository;

    private final S3Service s3Service;


    /**
    * 카테고리 타입: sns게시판
    * 카테고리:ID 1:동물공고 / 2:입양후기 / 3: sns홍보 / 4:실종,목격제보
    **/
    private static final Long SNS_CATEGORY_ID = 3L;

    /* SNS 게시판 글 등록 */
    @Transactional
    public SNSBoardDTO createSNSBoard(SNSBoardDTO boardDTO, List<MultipartFile> images, Long userId) {
        // 카테고리 확인
        Category category = categoryRepository.findById(SNS_CATEGORY_ID)
                .orElseThrow(() -> new IllegalArgumentException("SNS 카테고리를 찾을 수 없습니다."));

        // 인스타그램 링크 필수
        if (boardDTO.getInstagramLink() == null || boardDTO.getInstagramLink().trim().isEmpty()) {
            throw new IllegalArgumentException("인스타그램 링크는 필수입니다.");
        }

        // 사용자 유효성 체크
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(UNAUTHORIZED)));

        // 게시글 저장
        Board board = Board.builder()
                .category(category)
                .boardTitle(boardDTO.getBoardTitle())
                .boardContent(boardDTO.getBoardContent())
                .viewCount(0L)
                .instagramLink(boardDTO.getInstagramLink())
                .user(user)
                .build();
        snsBoardRepository.save(board);

        //이미지가 존재하면 이미지 업로드 및 이미지 엔티티 저장
        if (images != null && !images.isEmpty()) {
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

        // DB 반영 강제 flush
        snsBoardRepository.flush();

        // 저장된 게시글 + 이미지 포함 다시 조회
        Board fullBoard = snsBoardRepository.findById(board.getId())
                .orElseThrow(() -> new RuntimeException(String.valueOf(BOARD_NOT_FOUND)));

        System.out.println("게시글 생성 완료: 게시글 ID = " + fullBoard.getId());

        return toDto(fullBoard);
    }




    /* SNS 게시글 수정 */
    @Transactional
    public void updateSNSBoard(Long boardId, SNSBoardUpdateDTO dto, Long userId, List<MultipartFile> newImages) {

        // 1. 사용자 인증 및 게시글 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(UNAUTHORIZED)));

        Board board = snsBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(BOARD_NOT_FOUND)));

        if (board.getCategory() == null || !board.getCategory().getCategoryId().equals(SNS_CATEGORY_ID)) {
            throw new IllegalArgumentException(String.valueOf(CATEGORY_NOT_FOUND));
        }
        if (board.getDeletedAt() != null) {
            throw new IllegalArgumentException(String.valueOf(BOARD_ALLREDAY_DELETE));
        }
        if (!board.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException(String.valueOf(FORBIDDEN));
        }
        if (board.getInstagramLink() == null) {
            throw new IllegalArgumentException("SNS 게시글 링크가 사라졌습니다.");
        }

        // 2. 기존 이미지 처리 및 썸네일 지정
        List<Image> currentImages = board.getImages();
        List<Long> remainIds = dto.getRemainImageIds();
        Long thumbnailId = (remainIds != null && !remainIds.isEmpty()) ? remainIds.get(0) : null;

        for (Image image : currentImages) {
            if (!remainIds.contains(image.getImageId())) {
                image.softDelete();
            } else {
                if (image.getImageId().equals(thumbnailId)) {
                    image.markAsThumbnail();
                }
            }
            imageRepository.save(image);
        }

        // 3. 새 이미지 업로드
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

                imageRepository.save(image);
                uploadedImages.add(image);
            }
            board.getImages().addAll(uploadedImages);
        }

        //4. 게시글 정보 수정
        Board updatedBoard = board.toBuilder()
                .boardTitle(dto.getBoardTitle() != null ? dto.getBoardTitle() : board.getBoardTitle())
                .boardContent(dto.getBoardContent() != null ? dto.getBoardContent() : board.getBoardContent())
                .instagramLink(dto.getInstagramLink() != null ? dto.getInstagramLink() : board.getInstagramLink())
                .viewCount(dto.getViewCount() != null ? dto.getViewCount() : board.getViewCount())
                .images(board.getImages())
                .user(board.getUser())
                .build();

        snsBoardRepository.save(updatedBoard);
    }




    /* SNS 게시판 글 삭제 */
    @Transactional
    public void deleteSNSBoard(Long boardId,Long userId) {
        //board id 찾기
        Board board = snsBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(BOARD_NOT_FOUND)));
        //category id 확인
        if (board.getCategory() == null ||
                !board.getCategory().getCategoryId().equals(SNS_CATEGORY_ID)) {
            throw new IllegalArgumentException(String.valueOf(CATEGORY_NOT_FOUND));
        }
        // 이미 삭제된 게시글인지
        if (board.getDeletedAt() != null) {
            throw new IllegalArgumentException(String.valueOf(BOARD_ALLREDAY_DELETE));
        }
        // 작성자와 같은 사람인지
        if (!board.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException(String.valueOf(FORBIDDEN));
        }
        board.softDelete();
    }


    /* SNS 게시판 글 상세조회 */
    @Transactional
    public SNSBoardDTO getBoardDetail(Long boardId) {
        Board board = snsBoardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(BOARD_NOT_FOUND)));
        // 카테고리 확인
        if (!board.getCategory().getCategoryId().equals(SNS_CATEGORY_ID)) {
            throw new IllegalArgumentException(String.valueOf(CATEGORY_NOT_FOUND));
        }
        // 삭제된 글인지 확인
        if(board.getDeletedAt()!=null){
            throw new IllegalArgumentException(String.valueOf(BOARD_ALLREDAY_DELETE));
        }
        // image 가져옴
        List<String> visibleImages = board.getImages().stream()
                .filter(image -> image.getDeletedAt() == null)
                .map(Image::getS3Url)
                .collect(Collectors.toList());
        // 댓글가져옴
        List<SNSBoardDTO.CommentDTO> commentDTOList = board.getComments().stream()
                .filter(comment -> comment.getDeletedAt() == null)
                .map(SNSBoardDTO.CommentDTO::toDTO)
                .collect(Collectors.toList());
        //좋아요 수 조회
        Long likeCount = likeRepository.countByBoardId(boardId);

        return SNSBoardDTO.builder()
                .id(board.getId())
                .category(board.getCategory().getCategoryId())
                .userId(board.getUser() != null ? board.getUser().getId() : null)
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .instagramLink(board.getInstagramLink())
                .viewCount(board.getViewCount())
                .images(visibleImages)
                .comments(commentDTOList)
                .likeCount(likeCount)
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .build();
    }


    /** SNS 게시판 페이징 **/
    @Transactional(readOnly = true)
    public Page<SNSBoardDTO> getBoardsPaged(Pageable pageable) {
        // category 가 SNS 친구만 페이징 조회
        Page<Board> boardsPage = snsBoardRepository.findByCategory_CategoryIdAndDeletedAtIsNull(SNS_CATEGORY_ID, pageable);

        List<SNSBoardDTO> dtoList = boardsPage
                .map(this::toDto)
                .getContent();

        return new PageImpl<>(dtoList, pageable, boardsPage.getTotalElements());
    }

    /**
     * SNS 게시판 검색 페이징
     * @param keyword 검색 키워드 (제목 또는 내용)
     * @param pageable 페이징 정보
     * @return SNSBoardDTO 페이징 결과
     */
    @Transactional(readOnly = true)
    public Page<SNSBoardDTO> searchSNSBoards(String keyword, Pageable pageable) {
        Page<Board> boardsPage = snsBoardRepository.searchKeywordSNS(SNS_CATEGORY_ID, keyword, pageable);

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
                .createdAt(board.getCreatedAt())
                .category(board.getCategory().getCategoryId())
                .userId(board.getUser() != null ? board.getUser().getId() : null)
                .nickname(board.getUser() != null ? board.getUser().getNickname() : null)
                .images(board.getImages().stream()
                        .map(Image::getS3Url) // getUrl()은 이미지 엔티티의 S3 URL 반환 메서드
                        .toList())
                .build();
    }

    @Transactional
    public void increaseViewCount(Long boardId) {
        Board board = snsBoardRepository.findById(boardId)
                .orElseThrow();
        board.increaseViewCount();
    }


}
