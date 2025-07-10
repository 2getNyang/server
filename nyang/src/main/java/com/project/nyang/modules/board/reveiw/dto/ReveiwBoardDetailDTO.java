package com.project.nyang.modules.board.reveiw.dto;

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
        @Schema(description = "지역 이름")
        private String subRegionName;
        @Schema(description = "보호소 이름")
        private String careName;

        @Builder
        public PetApplicationDTO(Long formId, String kindFullNm, String age, String sexCd, LocalDate happenDt, String subRegionName, String careName) {
            this.formId = formId;
            this.kindFullNm = kindFullNm;
            this.age = age;
            this.sexCd = sexCd;
            this.happenDt = happenDt;
            this.subRegionName = subRegionName;
            this.careName = careName;
        }

        public static PetApplicationDTO toDTO(PetApplicationForm form) {
            return PetApplicationDTO.builder()
                    .formId(form.getId())
                    .kindFullNm(form.getAnimal().getKindFullNm())
                    .age(form.getAnimal().getAge())
                    .sexCd(form.getAnimal().getSexCd())
                    .happenDt(form.getAnimal().getHappenDt())
                    .subRegionName(form.getAnimal().getShelter().getSubRegion().getSubRegionName())
                    .careName(form.getAnimal().getShelter().getCareName())
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

        public static ImageDTO toDTO(Image  image) {
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
        @Schema(description = "부모 댓글 ID")
        private Long parentId;

        @Builder
        public CommentDTO(String commnetContent, LocalDateTime createdAt, String commentNickname, Long parentId, Long id) {
            this.id = id;
            this.commnetContent = commnetContent;
            this.createdAt = createdAt;
            this.commentNickname = commentNickname;
            this.parentId = parentId;
        }

        public static CommentDTO toDTO(Comment comment) {
            return CommentDTO.builder()
                    .commnetContent(comment.getCommentContent())
                    .createdAt(comment.getCreatedAt())
                    .commentNickname(comment.getUser().getNickname())
                    .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                    .id(comment.getId())
                    .build();
        }
    }

    @Builder(toBuilder = true)
    public ReveiwBoardDetailDTO(String nickname, Long userId, String boardTitle, String boardContent, LocalDateTime createdAt, Long boardViewCount, int likeItCount, Boolean isLiked, PetApplicationDTO petApplicationDTO, List<CommentDTO> comments, List<ImageDTO> images) {
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