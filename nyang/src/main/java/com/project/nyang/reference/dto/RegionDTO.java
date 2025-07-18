package com.project.nyang.reference.dto;

import com.project.nyang.reference.entity.Region;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 시/도 이름 정보 dto 입니다.
 *
 * @author : 오승훈
 * @fileName : RegionDTO
 * @since : 2025-07-15
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {
    private String regionCode;
    private String regionName;

    public RegionDTO(String regionName) {
        this.regionName = regionName;
    }

    public static RegionDTO from(Region region) {
        return RegionDTO.builder()
                .regionCode(region.getRegionCode())
                .regionName(region.getRegionName())
                .build();
    }
}