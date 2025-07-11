package com.project.nyang.modules.board.lost.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.project.nyang.global.common.S3.S3Service;
import com.project.nyang.global.exception.CustomException;
import com.project.nyang.global.exception.ErrorCode;
import com.project.nyang.modules.board.Board;
import com.project.nyang.modules.board.lost.dto.LostCreateRequestDto;
import com.project.nyang.modules.board.lost.dto.LostDetailResponseDTO;
import com.project.nyang.modules.board.lost.dto.LostListResponseDTO;
import com.project.nyang.modules.board.lost.repository.LostRepository;
import com.project.nyang.modules.image.entity.Image;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.modules.user.repository.UserRepository;
import com.project.nyang.reference.entity.*;
import com.project.nyang.reference.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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

    private final S3Service s3Service;
    private final Long CATEGORY_ID = 4L;

    //실종/목격 게시판의 모든 글 가져오는 메서드(페이징 처리완료)
    public Page<LostListResponseDTO> getLostBoard(Long categoryId, Pageable pageable) {
        Page<Board> boards = lostRepository.findByCategory_CategoryIdAndDeletedAtIsNull(categoryId, pageable);

        return boards.map(board -> {
            String thumbnailUrl = board.getImages().stream()
                    .filter(image -> "Y".equalsIgnoreCase(image.getThumbnailIs()))
                    .findFirst()
                    .map(Image::getS3Url)
                    .orElse(null);

            return LostListResponseDTO.builder()
                    .boardId(board.getId())
                    .categoryId(board.getCategory().getCategoryId())
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
                    .build();
        });
    }

    //실종/목격 게시판의 특정 글 조회 메서드
    public LostDetailResponseDTO getLostDetail(Long boardId) {

        Board board = lostRepository.findWithDetailsById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        return LostDetailResponseDTO.builder()
                .boardId(board.getId())
                .userId(board.getUser().getId())
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
                .createdAt(board.getCreatedAt())
                .imageUrls(board.getImages().stream()
                        .map(Image::getS3Url) // getUrl()은 이미지 엔티티의 S3 URL 반환 메서드
                        .toList())
                .build();
    }

    //실종/목격게시판 글작성 + 이미지 db저장 + s3 이미지 업로드
    @Transactional
    public Long createLostBoard(LostCreateRequestDto dto, List<MultipartFile> images) {

        // 1. 연관 엔티티 유효성 체크
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

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
            List<Image> imageEntities = new ArrayList<>();

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

        //4. 이미지 리스트를 Board에 반영
        lostRepository.save(board); //casecade = ALL 설정했기떄문에 IMAGES도 자동 저장된다!

        return board.getId();
    }

    //게시글 삭제 + 관련 이미지 삭제 + s3 이미지 삭제
    @Transactional
    public void deleteLostBoard(Long boardId){
        Board board = lostRepository.findById(boardId)
                .orElseThrow(()-> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        //1. S3 이미지 삭제
        for(Image image : board.getImages()){
            String s3Url = image.getS3Url(); //이미지의 s3 url 추출
            String fileName = extractFileNameFromUrl(s3Url);    //s3 url에서 이미지명+확장자만 추출
            s3Service.deleteFile(fileName);
        }

        //2. DB에서 게시글 + 연관 이미지 삭제
        lostRepository.delete(board);   //casecade + orphanRemoval 설정해둬서 함께 삭제가능!
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
}
