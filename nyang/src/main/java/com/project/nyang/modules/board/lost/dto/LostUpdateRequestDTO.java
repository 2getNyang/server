package com.project.nyang.modules.board.lost.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 글 수정 DTO입니다.
 *
 * @author : 선순주
 * @fileName : LostUpdateRequestDTO
 * @since : 2025-07-13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LostUpdateRequestDTO {
    @Schema(description = "게시글 ID", example = "12")
    private Long boardId;
    @Schema(description = "유저 ID", example = "1")
    private Long userId;
    @Schema(description = "카테고리 ID", example = "4")
    private Long categoryId;

    @Schema(description = "게시글 본문", example = "부천에서 잃어버린 고양이를 찾습니다.")
    private String content;
    @Schema(description = "게시글 타입. (MS:실종 / WT:목격)", example = "MS")
    private String lostType;
    @Schema(description = "실종/목격 장소", example = "부천역 3번 출구 근처")
    private String missingLocation;
    @Schema(description = "실종/목격 날짜", example = "2025-07-12")
    private LocalDate missingDate;

    @Schema(description = "시/도 이름", example = "경기도")
    private String regionName;
    @Schema(description = "시/도 코드", example = "6410000")
    private String RegionCode;
    @Schema(description = "시/군/구 이름", example = "부천시")
    private String subRegionName;
    @Schema(description = "시/군/구 코드", example = "3860000")
    private String subRegionCode;
    @Schema(description = "작성자 연락처", example = "010-2222-3333")
    private String phone;
    @Schema(description = "축종", example = "고양이")
    private String upKindName;
    @Schema(description = "축종 코드 (417000:개 / 422400:고양이 / 429900: 기타)", example = "417000")
    private String upKindCode;
    @Schema(description = "품종", example = "레그돌")
    private String kindName;
    @Schema(description = "품종 코드", example = "00213")
    private String kindCode;
    @Schema(description = "성별(M:수컷 / F:암컷 / Q:모름)", example = "F")
    private String gender;
    @Schema(description = "나이", example = "3")
    private Integer age;
    @Schema(description = "털 색깔", example = "갈색")
    private String furColor;
    @Schema(description = "특징", example = "한쪽 귀가 접혀 있어요.")
    private String distinctFeatures;
    @Schema(
            description = "유지할 이미지 ID 리스트 (기존 이미지 중 삭제하지 않을 이미지의 imageId 목록)",
            example = "[11, 12, 13]"
    )
    private List<Long> remainImageIds; // 유지할 이미지 ID 목록

}