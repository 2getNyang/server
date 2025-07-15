package com.project.nyang.modules.board.elasticsearch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 실종/목격 게시판 리스트 DTO
 *
 * @author : 박세정
 * @fileName : LostBoardListDTO
 * @since : 2025-07-15
 */
@Getter
public class LostBoardListDTO {
    @Schema(description = "게시글 ID")
    private String id;
    @Schema(description = "조회수")
    private Long boardViewCount;
    @Schema(description = "카테고리 ID")
    private Long categoryId;
    @Schema(description = "게시글 타입. (MS:실종 / WT:목격)", example = "MS")
    private String lostType;    //MS : 실종 or WT : 목격
    @Schema(description = "품종", example = "레그돌")
    private String kindName;    //품종 -> 실종/목격 게시판의 경우 해당 필드가 title 역할
    @Schema(description = "성별(M:수컷 / F:암컷 / Q:모름)", example = "F")
    private String gender;  // M:수컷 / F : 암컷 / Q : 모름
    @Schema(description = "나이", example = "3")
    private Integer age;
    @Schema(description = "털 색깔", example = "갈색")
    private String furColor;
    @Schema(description = "실종/목격 장소", example = "부천역 3번 출구 근처")
    private String missingLocation;
    @Schema(description = "실종/목격 날짜", example = "2025-07-12")
    private LocalDate missingDate;
    @Schema(description = "대표 이미지 url")
    private String imageUrl;

    @Builder
    public LostBoardListDTO(String id, Long boardViewCount, Long categoryId, String lostType, String kindName, String gender, Integer age, String furColor, String missingLocation, LocalDate missingDate, String imageUrl) {
        this.id = id;
        this.boardViewCount = boardViewCount;
        this.categoryId = categoryId;
        this.lostType = lostType;
        this.kindName = kindName;
        this.gender = gender;
        this.age = age;
        this.furColor = furColor;
        this.missingLocation = missingLocation;
        this.missingDate = missingDate;
        this.imageUrl = imageUrl;
    }
}