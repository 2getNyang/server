package com.project.nyang.reference.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 시/군/구 이름 정보 dto 입니다.
 *
 * @author : 오승훈
 * @fileName : SubRegionDTO
 * @since : 2025-07-15
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubRegionDTO {
    @Schema(description = "시군구 이름", example = "강남구")
    private String subRegionName;  // 시군구 이름만 포함
}