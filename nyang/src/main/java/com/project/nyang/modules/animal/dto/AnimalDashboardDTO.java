package com.project.nyang.modules.animal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 메인페이지용 DTO입니다.
 *
 * @author : 선순주
 * @fileName : AnimalDashboardDTO
 * @since : 2025-07-19
 */

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "대시보드 통계 데이터 DTO")
public class AnimalDashboardDTO {
    @Schema(description = "등록된 보호소 수", example = "120")
    private long shelterCount;

    @Schema(description = "현재 보호 중인 동물 수", example = "845")
    private long protectedAnimalCount;

    @Schema(description = "입양되었거나 반환된 동물 수", example = "302")
    private long adoptedOrReturnedCount;
}