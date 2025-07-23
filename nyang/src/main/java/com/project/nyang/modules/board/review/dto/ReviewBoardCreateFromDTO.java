package com.project.nyang.modules.board.review.dto;

import com.project.nyang.modules.adoption.entity.PetApplicationForm;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 입양 후기 게시글 생성 시 보여줄 생성폼
 *
 * @author : 선순주
 * @fileName : ReviewBoardCreateFromDTO
 * @since : 2025-07-19
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewBoardCreateFromDTO {
    private Long id;
    private Long userId;
    private List<PetApplicationDTO> petApplicationDTO;

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