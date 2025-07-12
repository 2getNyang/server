package com.project.nyang.modules.shelter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ShelterListDTO에 대한 클래스입니다
 * 보호소 전체 리스트 조회에 쓰이는 DTO입니다..
 *
 * @author : 오승훈
 * @fileName : ShelterListDTO
 * @since : 2025-07-10
 */
@Data
@AllArgsConstructor
public class ShelterListDTO {
    @Schema(description = "보호소 이름", example = "서울 동물보호소")
    private String careName;//보호소 이름

    @Schema(description = "보호소 전화번호", example = "02-1234-5678")
    private String careTel;//보호소 전화번호

    @Schema(description = "시/도 이름", example = "서울특별시")
    private String regionName;//시/도 이름

    @Schema(description = "시/군/구 이름", example = "강남구")
    private String subRegionName;// 시/군/구 이름
}