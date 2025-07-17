package com.project.nyang.modules.board.sns.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * sns board update dto
 *
 * @author : 이은서
 * @fileName : SNSBoardUpdateDTO
 * @since : 25. 7. 14.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SNSBoardUpdateDTO {
    @Schema(description = "게시글 ID", example = "12")
    private Long boardId;
    @Schema(description = "유저 ID", example = "1")
    private Long userId;
    @Schema(description = "카테고리 ID", example = "3")
    private Long categoryId;
    @Schema(description = "게시글 제목", example = "안녕하세요 우리집 고양이좀봐줘!!!!!!!!!!")
    private String boardTitle;
    @Schema(description = "게시글 본문", example = "우리집 달래 이쁘죠?")
    private String boardContent;
    @Schema(description = "인스타그램 게시글 링크 (공개게시글 + 끝에 / 붙여주세요)",
            example = "https://www.instagram.com/p/DMAv23SzJYg/?utm_source=ig_web_button_share_sheet&igsh=MzRlODBiNWFlZA==/")
    private String instagramLink;
    @Schema(
            description = "유지할 이미지 ID 리스트 (기존 이미지 중 삭제하지 않을 이미지의 imageId 목록)",
            example = "[11, 12, 13]"
    )
    private List<Long> remainImageIds;

    @Schema(description = "조회수",example = "123")
    private Long viewCount;

    private Long thumbnailImageId; // 새로 썸네일로 지정할 이미지의 ID


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

