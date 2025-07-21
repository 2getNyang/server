package com.project.nyang.modules.board.sns.dto;

import com.project.nyang.modules.board.lost.dto.LostUpdateResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * SNS홍보 게시판의 수정폼 DTO입니다.
 *
 * @author : 선순주
 * @fileName : SNSBoardUpdateFormDTO
 * @since : 2025-07-19
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SNSBoardUpdateFormDTO {
    @Schema(description = "카테고리 ID", example = "4")
    private Long categoryId;
    @Schema(description = "게시글 ID", example = "12")
    private Long boardId;
    @Schema(description = "유저 ID", example = "1")
    private Long userId;

    @Schema(description = "게시글 제목", example = "안녕하세요 우리집 고양이좀봐줘!!!!!!!!!!")
    private String boardTitle;
    @Schema(description = "게시글 본문", example = "우리집 달래 이쁘죠?")
    private String boardContent;
    @Schema(description = "인스타그램 게시글 링크 (공개게시글 + 끝에 / 붙여주세요)",
            example = "https://www.instagram.com/p/DMAv23SzJYg/?utm_source=ig_web_button_share_sheet&igsh=MzRlODBiNWFlZA==/")
    private String instagramLink;
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
}