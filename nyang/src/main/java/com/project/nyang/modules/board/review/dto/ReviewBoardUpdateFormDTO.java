package com.project.nyang.modules.board.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 입양 후기 게시판 수정폼
 *
 * @author : 선순주
 * @fileName : ReviewBoardUpdateFormDTO
 * @since : 2025-07-20
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewBoardUpdateFormDTO {
    @Schema(description = "게시글 ID", example = "12")
    private Long boardId;
    @Schema(description = "유저 ID", example = "1")
    private Long userId;
    @Schema(description = "게시글 제목", example = "안녕하세요")
    private String boardTitle;
    @Schema(description = "게시글 본문", example = "우리집 달래 이쁘죠?")
    private String boardContent;
    @Schema(description = "입양 공고 관련")
    private PetApplicationDTO petApplicationDTO;
    @Schema(description = "이미지 S3 url 리스트", example = "[\"https://s3.amazonaws.com/bucket/image1.jpg\", \"https://s3.amazonaws.com/bucket/image2.jpg\"]")
    private List<ImageInfo> images;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    //이미지 정보 관련 정적 내부 클래스
    public static class ImageInfo {

        private Long imageId;       // 이미지 ID (soft delete 대상 식별용)
        private String imageUrl;    // S3 업로드 URL (프론트에서 미리보기 용도)
        private boolean isThumbnail; // 썸네일 여부 (프론트에서 표시용)

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PetApplicationDTO {
        // =========== 신청서 내용 ===========
        @Schema(description = "신청 번호")
        private Long formId;
        @Schema(description = "공고 번호")
        private String noticeNo;
        @Schema(description = "동물 전체 이름")
        private String kindFullNm;
        @Schema(description = "성별")
        private String sexCd;
        @Schema(description = "시/군/구 이름")
        private String subRegionName;
        @Schema(description = "시/도 이름")
        private String regionName;
        @Schema(description = "썸네일")
        private String profile1;
        @Schema(description = "신청일자")
        private LocalDateTime formCreateAt;
    }
}