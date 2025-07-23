package com.project.nyang.reference.dto;

import com.project.nyang.reference.entity.SubRegion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 시/군/구 이름 정보 dto 입니다.
 *
 * @author : 오승훈
 * @fileName : SubRegionDTO
 * @since : 2025-07-15
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubRegionDTO {
    @Schema(description = "시군구 이름", example = "강남구")
    private String subRegionName;  // 시군구 이름만 포함
    @Schema(description = "시군구 코드", example = "3220000")
    private String subRegionCode;

    public SubRegionDTO(String subRegionName) {
        this.subRegionName = subRegionName;
    }

    public static SubRegionDTO from(SubRegion subRegion) {
        return SubRegionDTO.builder()
                .subRegionCode(subRegion.getSubRegionCode())
                .subRegionName(subRegion.getSubRegionName())
                .build();
    }
}
