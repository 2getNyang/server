package com.project.nyang.global.elasticsearch.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * 실종/목격 게시판 리스트 DTO
 *
 * @author : 박세정
 * @fileName : LostBoardListDTO
 * @since : 2025-07-15
 */
@Getter
@SuperBuilder
public class LostBoardListDTO extends BoardListDTO{
    @Schema(description = "게시글 타입. (MS:실종 / WT:목격)", example = "MS")
    private String lostType;

    @Schema(description = "품종", example = "렉돌")
    private String kindName;

    @Schema(description = "성별(M:수컷 / F:암컷 / Q:모름)", example = "F")
    private String gender;

    @Schema(description = "나이", example = "3")
    private Integer age;

    @Schema(description = "털 색깔", example = "갈색")
    private String furColor;

    @Schema(description = "실종/목격 장소", example = "부천역 3번 출구 근처")
    private String missingLocation;

    @Schema(description = "실종/목격 날짜", example = "2025-07-12")
    private String missingDate;
    
    @Schema(description = "특징")
    private String distinctFeatures;

    public LostBoardListDTO(
            String id,
            Long viewCount,
            Long categoryId,
            String boardContent,
            String createdAt,
            String imageUrl,
            String nickname,
            String lostType,
            String kindName,
            String gender,
            Integer age,
            String furColor,
            String missingLocation,
            String missingDate,
            String distinctFeatures
    ) {
        super(id, viewCount, categoryId, "",boardContent, createdAt, imageUrl, nickname);
        this.lostType = lostType;
        this.kindName = kindName;
        this.gender = gender;
        this.age = age;
        this.furColor = furColor;
        this.missingLocation = missingLocation;
        this.missingDate = missingDate;
        this.distinctFeatures = distinctFeatures;
    }
}