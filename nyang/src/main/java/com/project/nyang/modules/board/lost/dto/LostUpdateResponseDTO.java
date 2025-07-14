package com.project.nyang.modules.board.lost.dto;

import com.project.nyang.modules.board.Board;
import com.project.nyang.modules.image.entity.Image;
import com.project.nyang.modules.user.entity.User;
import com.project.nyang.reference.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 글 수정 폼 DTO입니다.
 *
 * @author : 선순주
 * @fileName : LostUpdateResponseDTO
 * @since : 2025-07-13
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LostUpdateResponseDTO {
    @Schema(description = "카테고리 ID", example = "4")
    private Long categoryId;
    @Schema(description = "게시글 ID", example = "12")
    private Long boardId;
    @Schema(description = "유저 ID", example = "1")
    private Long userId;

    @Schema(description = "게시글 타입. (MS:실종 / WT:목격)", example = "MS")
    private String lostType;
    @Schema(description = "실종/목격 날짜", example = "2025-07-12")
    private LocalDate missingDate;
    @Schema(description = "실종/목격 장소", example = "부천역 3번 출구 근처")
    private String missingLocation;
    @Schema(description = "시/도 이름", example = "경기도")
    private String regionName;
    @Schema(description = "시/군/구 이름", example = "부천시")
    private String subRegionName;
    @Schema(description = "작성자 연락처", example = "010-2222-3333")
    private String phone;
    @Schema(description = "축종", example = "고양이")
    private String upKindName;      //축종
    @Schema(description = "품종", example = "레그돌")
    private String kindName;        //품종
    @Schema(description = "성별(M:수컷 / F:암컷 / Q:모름)", example = "F")
    private String gender;
    @Schema(description = "나이", example = "3")
    private Integer age;
    @Schema(description = "털 색깔", example = "갈색")
    private String furColor;
    @Schema(description = "게시글 본문", example = "부천에서 잃어버린 고양이를 찾습니다.")
    private String content;
    @Schema(description = "특징", example = "한쪽 귀가 접혀 있어요.")
    private String distinctFeatures;
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

