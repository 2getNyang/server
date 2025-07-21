package com.project.nyang.modules.board.review.dto;

import com.project.nyang.modules.adoption.entity.PetApplicationForm;
import com.project.nyang.modules.comment.entity.Comment;
import com.project.nyang.modules.image.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 입양 후기 게시판 리스트 요청 response DTO
 *
 * @author : 박세정
 * @fileName : ReveiwBoardListDTO
 * @since : 2025-07-09
 */
@Getter
public class ReveiwBoardDetailDTO {
    @Schema(description = "게시글 아이디")
    private Long id;
    @Schema(description = "사용자 닉네임")
    private String nickname;
    @Schema(description = "사용자 아이디")
    private Long userId;
    @Schema(description = "게시글 제목")
    private String boardTitle;
    @Schema(description = "게시글 내용")
    private String boardContent;
    @Schema(description = "생성 시간")
    private LocalDateTime createdAt;
    @Schema(description = "조회수")
    private Long boardViewCount;
    @Schema(description = "좋아요 수")
    private int likeItCount;
    @Schema(description = "좋아요 여부")
    private Boolean isLiked;
    @Schema(description = "신청서 내용")
    PetApplicationDTO petApplicationDTO;
    @Schema(description = "댓글 리스트")
    List<CommentDTO> comments;
    @Schema(description = "이미지 리스트")
    List<ImageDTO> images;

    @Getter
    public static class PetApplicationDTO {
        // =========== 신청서 내용 ===========
        @Schema(description = "유기 동물 번호")
        private String desertionNo;
        @Schema(description = "신청 번호")
        private Long formId;
        @Schema(description = "동물 전체 이름")
        private String kindFullNm;
        @Schema(description = "나이")
        private String age;
        @Schema(description = "성별")
        private String sexCd;
        @Schema(description = "구조일")
        private LocalDate happenDt;
        @Schema(description = "시/도 이름")
        private String regionName;
        @Schema(description = "시/군/구 이름")
        private String subRegionName;
        @Schema(description = "보호소 이름")
        private String careName;
        @Schema(description = "공고 번호")
        private String noticeNo;
        @Schema(description = "썸네일")
        private String profile1;
        @Schema(description = "신청일자")
        private LocalDateTime formCreateAt;

        @Builder
        public PetApplicationDTO(String desertionNo, Long formId, String kindFullNm, String age, String sexCd,
                                 String noticeNo, String profile1, LocalDate happenDt, String regionName, String subRegionName, String careName, LocalDateTime formCreateAt) {
            this.desertionNo = desertionNo;
            this.formId = formId;
            this.kindFullNm = kindFullNm;
            this.age = age;
            this.sexCd = sexCd;
            this.happenDt = happenDt;
            this.regionName = regionName;
            this.subRegionName = subRegionName;
            this.careName = careName;
            this.noticeNo = noticeNo;
            this.profile1 = profile1;
            this.formCreateAt = formCreateAt;
        }

        public static PetApplicationDTO toDTO(PetApplicationForm form) {
            return PetApplicationDTO.builder()
                    .desertionNo(form.getAnimal().getDesertionNo())
                    .formId(form.getFormId())
                    .kindFullNm(form.getAnimal().getKindFullNm())
                    .age(form.getAnimal().getAge())
                    .sexCd(form.getAnimal().getSexCd())
                    .happenDt(form.getAnimal().getHappenDt())
                    .regionName(form.getAnimal().getShelter().getRegion().getRegionName())
                    .subRegionName(form.getAnimal().getShelter().getSubRegion().getSubRegionName())
                    .careName(form.getAnimal().getShelter().getCareName())
                    .noticeNo(form.getAnimal().getNoticeNo())
                    .profile1(form.getAnimal().getPopfile1())
                    .formCreateAt(form.getFormCreatedAt())
                    .build();
        }
    }

    @Getter
    public static class ImageDTO {
        private String thumbnailIs;
        private String s3Url;
        private String originFileName;

        @Builder
        public ImageDTO(String thumbnailIs, String s3Url, String originFileName) {
            this.thumbnailIs = thumbnailIs;
            this.s3Url = s3Url;
            this.originFileName = originFileName;
        }

        public static ImageDTO toDTO(Image image) {
            return ImageDTO.builder()
                    .thumbnailIs(image.getThumbnailIs())
                    .s3Url(image.getS3Url())
                    .originFileName(image.getOriginFileName())
                    .build();
        }
    }

    @Getter
    public static class CommentDTO {
        @Schema(description = "댓글 ID")
        private Long id;
        @Schema(description = "댓글 내용")
        private String commnetContent;
        @Schema(description = "댓글 생성 시간")
        private LocalDateTime createdAt;
        @Schema(description = "댓글 작성자")
        private String commentNickname;
        @Schema(description = "댓글 작성자 id")
        private Long commentUserId;
        @Schema(description = "부모 댓글 ID")
        private Long parentId;

        @Builder
        public CommentDTO(String commnetContent, LocalDateTime createdAt, String commentNickname, Long parentId, Long id, Long commentUserId) {
            this.id = id;
            this.commnetContent = commnetContent;
            this.createdAt = createdAt;
            this.commentNickname = commentNickname;
            this.parentId = parentId;
            this.commentUserId = commentUserId;
        }

        public static CommentDTO toDTO(Comment comment) {
            return CommentDTO.builder()
                    .commnetContent(comment.getCommentContent())
                    .createdAt(comment.getCreatedAt())
                    .commentNickname(comment.getUser().getNickname())
                    .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                    .id(comment.getId())
                    .commentUserId(comment.getUser().getId())
                    .build();
        }
    }

    @Builder(toBuilder = true)
    public ReveiwBoardDetailDTO(Long id, String nickname, Long userId, String boardTitle, String boardContent, LocalDateTime createdAt, Long boardViewCount, int likeItCount, Boolean isLiked, PetApplicationDTO petApplicationDTO, List<CommentDTO> comments, List<ImageDTO> images) {
        this.id = id;
        this.nickname = nickname;
        this.userId = userId;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.createdAt = createdAt;
        this.boardViewCount = boardViewCount;
        this.likeItCount = likeItCount;
        this.isLiked = isLiked;
        this.petApplicationDTO = petApplicationDTO;
        this.comments = comments;
        this.images = images;
    }
}