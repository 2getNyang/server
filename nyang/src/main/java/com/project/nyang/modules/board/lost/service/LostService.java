package com.project.nyang.modules.board.lost.service;

import com.project.nyang.global.common.S3.S3Service;
import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.global.security.core.CustomUserDetails;
import com.project.nyang.modules.board.elasticsearch.dto.BoardEsDocument;
import com.project.nyang.modules.board.elasticsearch.repository.BoardEsRepository;
import com.project.nyang.modules.board.elasticsearch.service.BoardEsService;
import com.project.nyang.modules.board.entity.Board;
import com.project.nyang.modules.board.lost.dto.*;
import com.project.nyang.modules.board.lost.repository.LostRepository;
import com.project.nyang.modules.comment.repository.CommentRepository;
import com.project.nyang.modules.image.entity.Image;
import com.project.nyang.modules.like.repository.LikeRepository;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import com.project.nyang.reference.entity.*;
import com.project.nyang.reference.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.nyang.global.exception.ErrorCode.*;

/**
 * 실종/목격 게시판 서비스입니다.
 *
 * @author : 선순주
 * @fileName : LostService
 * @since : 2025-07-09
 */

@Service
@RequiredArgsConstructor
public class LostService {

    private final LostRepository lostRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RegionRepository regionRepository;
    private final SubRegionRepository subRegionRepository;
    private final UpkindRepository upkindRepository;
    private final KindRepository kindRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    private final S3Service s3Service;
    private final Long CATEGORY_ID = 4L;
    private final BoardEsRepository boardEsRepository;
    private final BoardEsService boardEsService;

    //실종/목격 게시판의 모든 글 가져오는 메서드(페이징 처리완료)
    public Page<LostListResponseDTO> getLostBoard(Long categoryId, Pageable pageable) {
        Page<Board> boards = lostRepository.findByCategory_CategoryIdAndDeletedAtIsNull(categoryId, pageable);

        return boards.map(board -> {
            //썸네일 여부가 Y인 이미지 한개 가져오는 메서드
            String thumbnailUrl = board.getImages().stream()
                    .filter(image -> "Y".equalsIgnoreCase(image.getThumbnailIs()))
                    .findFirst()
                    .map(Image::getS3Url)
                    .orElse(null);

            return LostListResponseDTO.builder()
                    .id(board.getId())
                    .categoryId(board.getCategory().getCategoryId())
                    .userId(board.getUser().getId())
                    .nickName(board.getUser().getNickname())
                    .lostType(
                            "MS".equalsIgnoreCase(board.getLostType()) ? "실종" :
                                    "WT".equalsIgnoreCase(board.getLostType()) ? "목격" : null
                    )
                    .kindName(board.getKind().getKindNm())
                    .gender(
                            "M".equalsIgnoreCase(board.getGender()) ? "수컷" :
                                    "F".equalsIgnoreCase(board.getGender()) ? "암컷" :
                                            "Q".equalsIgnoreCase(board.getGender()) ? "모름" : null)
                    .age(board.getAge())
                    .furColor(board.getFurColor())
                    .missingLocation(board.getMissingLocation())
                    .missingDate(board.getMissingDate())
                    .viewCount(board.getViewCount())
                    .thumbnailUrl(thumbnailUrl)
                    .createdAt(board.getCreatedAt())
                    .deleteAt(board.getDeletedAt())
                    .build();
        });
    }

    //실종/목격 게시판의 특정 글 조회 메서드
    @Transactional  //조회수 증가 dirth checking을 위한 @Transactional 어노테이션 추가
    public LostDetailResponseDTO getLostDetail(Long boardId) {

        Board board = lostRepository.findWithDetailsById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        if (board.getDeletedAt() != null) {
            throw new CustomException(ErrorCode.BOARD_ALLREDAY_DELETE);
        }
        List<LostDetailResponseDTO.CommentDTO> commentDTOList = board.getComments().stream()
                .filter(comment -> comment.getDeletedAt() == null)
                .map(LostDetailResponseDTO.CommentDTO::toDTO)
                .collect(Collectors.toList());
        
        //조회수 증가
        board.increaseViewCount();

        //좋아요 수 조회
        Long likeCount = likeRepository.countByBoardId(boardId);

        //elastic search 조회수 증가
        /** elasticSearch 조회수 증가 */
        BoardEsDocument doc = boardEsRepository.findById(String.valueOf(board.getId())).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        doc.increaseViewCount();
        boardEsRepository.save(doc);

        return LostDetailResponseDTO.builder()
                .boardId(board.getId())
                .userId(board.getUser().getId())
                .nickName(board.getUser().getNickname())
                .lostType(
                        "MS".equalsIgnoreCase(board.getLostType()) ? "실종" :
                                "WT".equalsIgnoreCase(board.getLostType()) ? "목격" : null
                )
                .categoryId(board.getCategory().getCategoryId())
                .kindName(board.getKind() != null ? board.getKind().getKindNm() : null)
                .gender(
                        "M".equalsIgnoreCase(board.getGender()) ? "수컷" :
                                "F".equalsIgnoreCase(board.getGender()) ? "암컷" :
                                        "Q".equalsIgnoreCase(board.getGender()) ? "모름" : null)
                .age(board.getAge())
                .furColor(board.getFurColor())
                .regionName(board.getRegion() != null ? board.getRegion().getRegionName() : null)
                .subRegionName(board.getSubRegion() != null ? board.getSubRegion().getSubRegionName() : null)
                .missingLocation(board.getMissingLocation())
                .missingDate(board.getMissingDate())
                .phone(board.getPhone())
                .likeCount(likeCount)
                .comments(commentDTOList)
                .createdAt(board.getCreatedAt())
                .deletedAt(board.getDeletedAt())
                .imageUrls(board.getImages().stream()
                        .map(Image::getS3Url) // getUrl()은 이미지 엔티티의 S3 URL 반환 메서드
                        .toList())
                .build();
    }

    //실종/목격게시판 글작성 + 이미지 db저장 + s3 이미지 업로드
    @Transactional
    public Long createLostBoard(LostCreateRequestDto dto, List<MultipartFile> images) {

        // 인증된 사용자 가져오기
        Long userId = SecurityUtil.getCurrentUserId();

        //1. 연관 엔티티 유효성 검사
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(BAD_REQUEST)));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(CATEGORY_NOT_FOUND)));

        Region region = regionRepository.findByRegionName(dto.getRegionName())
                .orElse(null);

        SubRegion subRegion = subRegionRepository.findBySubRegionName(dto.getSubRegionName())
                .orElse(null);

        UpKind upKind = upkindRepository.findByUpKindNm(dto.getUpKindName())
                .orElse(null);

        Kind kind = kindRepository.findByKindNm(dto.getKindName())
                .orElse(null);

        //2. 게시글 먼저 저장(이미지는 일단 빈리스트로 넘김)
        Board board = dto.toEntity(user, category, new ArrayList<>(), region, subRegion, upKind, kind);
        lostRepository.save(board);

        // 3. 이미지 업로드 및 엔티티 생성

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
        BoardEsDocument doc = BoardEsDocument.builder()
                .id(String.valueOf(board.getId()))
                .viewCount(board.getViewCount())
                .categoryId(board.getCategory().getCategoryId())
                .lostType(board.getLostType())
                .kindName(board.getKind().getKindNm())
                .gender(board.getGender())
                .age(board.getAge())
                .furColor(board.getFurColor())
                .missingLocation(board.getMissingLocation())
                .missingDate(String.valueOf(board.getMissingDate()))
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

        return board.getId();
    }

    //물리적 삭제 : 게시글 삭제 + 관련 이미지 삭제 + s3 이미지 삭제
//    @Transactional
//    public void deleteLostBoard(Long boardId){
//        Board board = lostRepository.findById(boardId)
//                .orElseThrow(()-> new CustomException(ErrorCode.BOARD_NOT_FOUND));
//
//        if (board.getDeletedAt() != null) {
//            throw new CustomException(ErrorCode.BOARD_ALLREDAY_DELETE);
//        }
//
//        //1. S3 이미지 삭제
//        for(Image image : board.getImages()){
//            String s3Url = image.getS3Url(); //이미지의 s3 url 추출
//            String fileName = extractFileNameFromUrl(s3Url);    //s3 url에서 이미지명+확장자만 추출
//            s3Service.deleteFile(fileName);
//        }
//
//        //2. DB에서 게시글 + 연관 이미지 삭제
//        lostRepository.delete(board);   //casecade + orphanRemoval 설정해둬서 함께 삭제가능!
//    }

    //softDelete용 게시글 삭제
    @Transactional
    public LostDeleteResponseDTO softDeleteLostBoard(Long boardId){
        // 인증된 사용자 가져오기
        Long userId = SecurityUtil.getCurrentUserId();

        // 게시글 조회
        Board board = lostRepository.findById(boardId)
                .orElseThrow(()-> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        //삭제 여부 확인
        if (board.getDeletedAt() != null) {
            throw new CustomException(ErrorCode.BOARD_ALLREDAY_DELETE);
        }

        //작성자와 로그인한 사용자 일치 여부
        if (!board.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN); // 403: 권한 없음
        }
        // 게시글과 이미지 soft delete 수행(deleteAt에 타임스탬프)
        board.softDelete();
        // 하지만 elastic search 에는 걍 삭제합니다
        boardEsRepository.deleteById(String.valueOf(boardId));
        return new LostDeleteResponseDTO(board.getId(), board.getDeletedAt());
    }

    //게시글 수정폼 불러오는 메서드
    public LostUpdateResponseDTO getBoardUpdateForm(Long boardId) {

        // 인증된 사용자 가져오기
        Long userId = SecurityUtil.getCurrentUserId();

        Board board = lostRepository.findByIdAndDeletedAtIsNull(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다"));

        //작성자와 로그인한 사용자 일치 여부
        if (!board.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN); // 403: 권한 없음
        }

        List<LostUpdateResponseDTO.ImageInfo> images = board.getImages().stream()
                .filter(img -> img.getDeletedAt() == null)
                .map(img -> LostUpdateResponseDTO.ImageInfo.builder()
                        .imageId(img.getImageId())
                        .imageUrl(img.getS3Url())
                        .isThumbnail("Y".equalsIgnoreCase(img.getThumbnailIs()))
                        .build())
                .collect(Collectors.toList());

        return LostUpdateResponseDTO.builder()
                .categoryId(board.getCategory().getCategoryId())
                .boardId(board.getId())
                .userId(board.getUser().getId())
                .lostType(board.getLostType())
                .missingDate(board.getMissingDate())
                .missingLocation(board.getMissingLocation())
                .regionName(board.getRegion().getRegionName())
                .subRegionName(board.getSubRegion().getSubRegionName())
                .phone(board.getPhone())
                .upKindName(board.getUpKind().getUpKindNm())
                .kindName(board.getKind().getKindNm())
                .gender(board.getGender())
                .age(board.getAge())
                .furColor(board.getFurColor())
                .content(board.getBoardContent())
                .distinctFeatures(board.getDistinctFeatures())
                .images(images)
                .build();
    }

    //게시글 수정 메서드(이미지 삭제, 썸네일 처리도 포함)
    @Transactional
    public void updateBoard(Long boardId, LostUpdateRequestDTO dto, List<MultipartFile> newImages) {

        // 인증된 사용자 가져오기
        Long userId = SecurityUtil.getCurrentUserId();

        Board board = lostRepository.findByIdAndDeletedAtIsNull(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다"));

        //작성자와 로그인한 사용자 일치 여부
        if (!board.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN); // 403: 권한 없음
        }

        // 1. 연관 객체 조회 (코드 기반으로)
        Region region = regionRepository.findById(dto.getRegionCode()).orElseThrow(() -> new EntityNotFoundException("시도 코드가 없습니다.") );
        SubRegion subRegion = subRegionRepository.findById(dto.getSubRegionCode()).orElseThrow(() -> new EntityNotFoundException("시군구 코드가 없습니다."));
        Kind kind = kindRepository.findById(dto.getKindCode()).orElseThrow(() -> new EntityNotFoundException("축종이 없습니다."));
        UpKind upKind = upkindRepository.findById(dto.getUpKindCode()).orElseThrow(() -> new EntityNotFoundException("품종이 없습니다."));
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("카테고리가 없습니다"));

        // 2. 게시글 필드 업데이트
        board.updateBoardInfo(dto, region, subRegion, kind, upKind, category);

        // 3. 기존 이미지 중 삭제될 것들 soft delete
        List<Image> currentImages = board.getImages();
        List<Long> remainIds = dto.getRemainImageIds();
        boolean thumbnailDeleted = false;

        for (Image image : currentImages) {
            if (!remainIds.contains(image.getImageId())) {
                if ("Y".equalsIgnoreCase(image.getThumbnailIs())) {
                    thumbnailDeleted = true;
                }
                image.softDelete();
            }
        }

        // 4. 새 이미지 S3 업로드 및 Image 객체 생성
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

        // 5. 썸네일이 삭제되었을 경우 대체 썸네일 지정
        if (thumbnailDeleted) {
            Optional<Image> newThumbnail = currentImages.stream()
                    .filter(img -> img.getDeletedAt() == null)
                    .findFirst();

            if (newThumbnail.isEmpty()) {
                newThumbnail = uploadedImages.stream().findFirst();
            }

            newThumbnail.ifPresent(Image::markAsThumbnail);
        }
        /** elastic search 반영 **/
        BoardEsDocument doc = BoardEsDocument.builder()
                .id(String.valueOf(board.getId()))
                .viewCount(board.getViewCount())
                .categoryId(board.getCategory().getCategoryId())
                .lostType(board.getLostType())
                .kindName(board.getKind().getKindNm())
                .gender(board.getGender())
                .age(board.getAge())
                .furColor(board.getFurColor())
                .missingLocation(board.getMissingLocation())
                .missingDate(String.valueOf(board.getMissingDate()))
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

    //s3 이미지 경로에서 앞의 접두사를 빼고 온전히 이미지의 이름+확장자만 가져오게 하는 메서드
    private String extractFileNameFromUrl(String s3Url) {
        // ex) https://backend-nyang.s3.ap-northeast-2.amazonaws.com/images/abc123.jpg
        // ⇒ return "images/abc123.jpg"
        int index = s3Url.indexOf(".com/");
        if (index != -1) {
            return s3Url.substring(index + 5); // ".com/" 이후부터 끝까지
        } else {
            throw new IllegalArgumentException("Invalid S3 URL: " + s3Url);
        }
    }

    //인증된 사용자 정보 가져오는 메서드
    public class SecurityUtil {

        public static Long getCurrentUserId() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }

            Object principal = authentication.getPrincipal();

            if (!(principal instanceof CustomUserDetails)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }

            CustomUserDetails userDetails = (CustomUserDetails) principal;
            return userDetails.getId();
        }
    }
}
