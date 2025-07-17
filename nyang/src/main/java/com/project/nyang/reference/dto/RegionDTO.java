package com.project.nyang.reference.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 시/도 이름 정보 dto 입니다.
 *
 * @author : 오승훈
 * @fileName : RegionDTO
 * @since : 2025-07-15
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {
    @Schema(description = "시도 이름", example = "서울특별시")
    private String regionName;  // 시도 이름만 포함
}